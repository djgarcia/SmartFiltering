package org.apache.spark.feature

import org.apache.spark.feature.Keel.RNG
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

class RNG_BD(val data: RDD[LabeledPoint], val order: Boolean = true, val selType: Boolean = true) extends Serializable {

  def runFilter(): RDD[LabeledPoint] = {

    data.mapPartitions { partition =>
      val peristPartition = partition.toArray
      val dataAsArray = peristPartition.map(_.features.toArray)
      val classes = peristPartition.map(_.label.toInt)

      val selectedData = new RNG(dataAsArray, classes, order, selType).ejecutar()

      val filteredData = peristPartition.zipWithIndex.filter { x =>
        selectedData(x._2)
      }.map(_._1)

      filteredData.toIterator
    }
  }
}

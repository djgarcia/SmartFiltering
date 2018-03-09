package org.apache.spark.mllib.feature

import org.apache.spark.mllib.classification.kNN_IS.kNN_IS
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

class AllKNN_BD(val data: RDD[LabeledPoint], val k: Int) extends Serializable {

  def runFilter(): RDD[LabeledPoint] = {

    val numClass = data.map(_.label).distinct().collect().length
    val numFeatures = data.first().features.size
    var i = 1
    var goodClassified: RDD[LabeledPoint] = data

    do {
      val knn = kNN_IS.setup(goodClassified, goodClassified, i, 2, numClass, numFeatures, goodClassified.partitions.length, 64, -1, 1)

      val predictions = knn.predict(goodClassified.context).map(_._1).zipWithIndex.map { case (k, v) => (v, k) }

      val joinedData = data.zipWithIndex.map { case (k, v) => (v, k) }.join(predictions)

      goodClassified = joinedData.filter { case (key, (l, pred)) =>
        l.label == pred
      }.map { l => l._2._1 }

      i = i + 2
    } while (i <= k)

    goodClassified
  }
}

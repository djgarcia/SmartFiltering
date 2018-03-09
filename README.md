# SmartFiltering

This framework implements four distance based Big Data preprocessing algorithms to remove noisy examples: ENN_BD, AllKNN_BD, NCNEdit_BD and RNG_BD filters, with special emphasis in their scalability and performance traits.

## Example (ENN_BD)


```scala
import org.apache.spark.mllib.feature._

// Data must be cached in order to improve the performance

val enn_bd_model = new ENN_BD(trainingData, // RDD[LabeledPoint]
                              k) // number of neighbors

val enn_bd = enn_bd_model.runFilter()
```
## Example (AllKNN_BD)


```scala
import org.apache.spark.mllib.feature._

// Data must be cached in order to improve the performance

val allknn_bd_model = new AllKNN_BD(trainingData, // RDD[LabeledPoint]
                              k) // number of neighbors

val allknn_bd = allknn_bd_model.runFilter()
```

## Example (NCNEdit_BD)


```scala
import org.apache.spark.mllib.feature._

// Data must be cached in order to improve the performance

val ncnedit_bd_model = new NCNEdit_BD(trainingData, // RDD[LabeledPoint]
                              k) // number of neighbors

val ncnedit_bd = ncnedit_bd_model.runFilter()
```

## Example (RNG_BD)


```scala
import org.apache.spark.mllib.feature._

// Data must be cached in order to improve the performance

val rng_bd_model = new RNG_BD(trainingData, // RDD[LabeledPoint]
                              order, // Order of the graph (true = first, false = second)
                              selType) // Selection type (true = edition, false = condensation)

val rng_bd = rng_bd_model.runFilter()
```


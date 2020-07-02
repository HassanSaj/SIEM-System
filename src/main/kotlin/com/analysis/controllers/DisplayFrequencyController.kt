package com.analysis.controllers

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.chart.PieChart
import javafx.scene.layout.BorderPane


class DisplayFrequencyController {
    @FXML
    private lateinit var displayFrequencyPane: BorderPane

    private var data: Map<String, Int>? = null

    fun setData(data: Map<String, Int>) {
        this.data = data
    }

    fun displayChartData() {
        // create the observable list of pie chart data.
        val observableList = FXCollections.observableArrayList<PieChart.Data>()

        // convert the supplied data from the map to the observable list
        data?.entries?.forEach { observableList.add(PieChart.Data(it.key, it.value.toDouble())) }

        // create the pie chart with the pie-chart data
        val pieChart = PieChart(observableList)

        // place the bar chart at the center of the display frequency pane
        displayFrequencyPane.center = pieChart
    }
}

package com.analysis.controllers

import com.analysis.AnalysisApplication
import com.analysis.util.AlertUtil
import com.analysis.util.Delimiter
import com.analysis.util.GridFactory
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.SelectionMode
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import javafx.stage.Stage
import org.controlsfx.control.spreadsheet.SpreadsheetView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*

@Controller
class DisplayDataController : Initializable {

    @FXML
    private lateinit var displayDataPane: BorderPane

    @FXML
    private lateinit var groupColumnBtn: Button

    @FXML
    private lateinit var loadCsvBtn: Button

    @FXML
    private lateinit var csvDelimitersCBox: ComboBox<Delimiter>

    @Autowired
    private lateinit var alertUtil: AlertUtil

    @Autowired
    private lateinit var gridFactory: GridFactory

    private var currentSpreadSheetView: SpreadsheetView? = null

    /**
     * @return The current delimiter character.
     */
    val delimiterChar: Char
        get() = this.csvDelimitersCBox.selectionModel.selectedItem.delimiterChar


    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        // initialize the supported delimiters.
        val delimiters = FXCollections.observableArrayList(Delimiter("comma", ','),
                Delimiter("tab", '\t'),
                Delimiter("colon", ':'),
                Delimiter("semi-colon", ';'))

        // show the delimiters on the CBox
        this.csvDelimitersCBox.items = delimiters

        // show the default delimiters
        this.csvDelimitersCBox.selectionModel.selectFirst()
    }

    /**
     * Event handler for the 'loadCsvBtn' button.
     */
    fun handleLoadFileAction() {
        // load file
        val loadedFile = loadFile()
        if (loadedFile != null) { // ensure the file is non-null.
            try {
                // create the data grid.
                val grid = gridFactory.createGrid(loadedFile)

                // create and init the spread sheet.
                val spreadsheetView = SpreadsheetView()

                // only allow the selection of one cell at a time.
                spreadsheetView.selectionModel.selectionMode = SelectionMode.SINGLE

                // set the grid of the spreadsheet view.
                spreadsheetView.grid = grid

                // display the spreadsheet
                this.displayDataPane.center = spreadsheetView

                // save the current spreadsheet.
                this.currentSpreadSheetView = spreadsheetView
            } catch (e: IOException) {
                alertUtil.showErrorAlert("Error loading file! " + e.message)
                e.printStackTrace()
            }

        }
    }


    /**
     * Load the file via file chooser.
     * @return The loaded file or `null` if no file was loaded.
     */
    private fun loadFile(): File? {
        val fileChooser = FileChooser()
        fileChooser.title = "Select CSV file"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("CSV", "*.csv"))
        return fileChooser.showOpenDialog(null)
    }

    // group columns based on the selected column.
    fun groupColumn() {
        if (this.currentSpreadSheetView == null) {
            alertUtil.showErrorAlert("No valid file found!")
            return
        }
        // get the currently selected cell, and therefore the column.
        val tablePosition = this.currentSpreadSheetView!!.selectionModel
                .selectedCells
                .stream()
                .findFirst()
                .orElse(null)

        if (tablePosition == null) {
            alertUtil.showErrorAlert("No valid column found!")
        } else {
            try {
                // get the frequency data for the selected column.
                val frequencyData = getFrequencyData(tablePosition.column)

                // now load the display frequency window
                val fxmlLoader = FXMLLoader(AnalysisApplication::class.java.getResource("display_frequency.fxml"))

                // now load the FXML UI file
                val parent = fxmlLoader.load<Parent>()

                // get the display frequency controller
                val controller = fxmlLoader.getController<DisplayFrequencyController>()

                // ask the controller class to init the map data
                controller.setData(frequencyData)

                // now display the data
                controller.displayChartData()

                // create scene
                val scene = Scene(parent)

                // create stage
                val stage = Stage()

                // set the scene of the stage
                stage.scene = scene

                // show the stage
                stage.show()

            } catch (e: IOException) {
                alertUtil.showErrorAlert("System Error!")
                e.printStackTrace()
            }

        }
    }

    /**
     * Get the frequency data.
     * @param columnIndex The column index.
     * @return The frequency data as a map.
     */
    private fun getFrequencyData(columnIndex: Int): Map<String, Int> {
        // declare frequency data.
        val data = HashMap<String, Int>()

        // get all the rows
        val rows = this.currentSpreadSheetView!!.items
        for (row in rows) {
            // get the item in the specified column, for this row.
            val item = row[columnIndex].item as String

            if (data.containsKey(item)) { // the item already exists
                val count = data[item]!! + 1
                // add increment the count of the data item.
                data[item] = count
            } else {
                // it is the first time we are adding this item,
                data[item] = 1
            }

        }

        return data
    }


}

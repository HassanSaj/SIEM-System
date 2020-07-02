package com.analysis.util
import com.analysis.controllers.DisplayDataController
import javafx.collections.FXCollections
import javafx.collections.ObservableList



import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord


import org.controlsfx.control.spreadsheet.GridBase
import org.controlsfx.control.spreadsheet.SpreadsheetCell
import org.controlsfx.control.spreadsheet.SpreadsheetCellType




import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component




import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*

@Component
class GridFactory {

    @Autowired
    private lateinit var displayDataController: DisplayDataController

    @Throws(IOException::class)
    fun createGrid(file: File): GridBase {
        // get the data for the grid.
        val cellGridData = csvRecordsToRows(getRecords(file))

        // get the number of rows.
        val rowCount = cellGridData.size

        // get the number of columns.
        val sampleRow = cellGridData.stream().findFirst().orElse(null)
        val columnCount = sampleRow?.size ?: 0

        // create the grid.
        val gridBase = GridBase(rowCount, columnCount)

        // now set the data for the grid.
        gridBase.setRows(cellGridData)


        // return the grid.
        return gridBase
    }

    // convert the CSV record list to rows for the grid.
    private fun csvRecordsToRows(csvRecords: List<CSVRecord>): ObservableList<ObservableList<SpreadsheetCell>> {
        val rows = FXCollections.observableArrayList<ObservableList<SpreadsheetCell>>()
        var rowIndex = 0
        for (csvRecord in csvRecords) {
            rows.add(FXCollections.observableArrayList(csvRecordToSpreadSheetRow(csvRecord, rowIndex)))
            rowIndex++
        }
        return rows
    }

    // transform csv record to a spreadsheet row
    private fun csvRecordToSpreadSheetRow(csvRecord: CSVRecord, rowIndex: Int): List<SpreadsheetCell> {
        val spreadsheetCells = ArrayList<SpreadsheetCell>()
        for ((columnIndex, column) in csvRecord.withIndex()) {
            // create a spreadsheet cell.
            val cell = SpreadsheetCellType.STRING.createCell(rowIndex, columnIndex, 1, 1, column)

            // add the style class for the cell.
            cell.styleClass?.add("cell")

            // add cell to the row of cells.
            spreadsheetCells.add(cell)
        }

        return spreadsheetCells
    }

    // get records from the CSV records.
    @Throws(IOException::class)
    private fun getRecords(loadedFile: File): List<CSVRecord> {
        // create reader object.
        val reader = FileReader(loadedFile)

        // get the CSV delimited.
        val delimiterChar = this.displayDataController.delimiterChar

        // return the CSV records.
        return CSVFormat.newFormat(delimiterChar)
                .parse(reader)
                .records
    }


}

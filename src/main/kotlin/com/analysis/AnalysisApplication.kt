package com.analysis

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AnalysisApplication : Application() {

	override fun start(stage: Stage?) {
		// run to get spring context
		val context = runApplication<AnalysisApplication>()

		// create the loader to load FXML file
		val loader = FXMLLoader(AnalysisApplication::class.java.getResource("display_data.fxml"))

		// set the controller factory class.
		loader.setControllerFactory { context.getBean(it) }

		// load the fxml file
		val parent = loader.load<AnchorPane>()

		// create the scene
		val scene = Scene(parent)

		// configure the stage
		stage?.scene = scene

		// show the stage
		stage?.show()

	}

	companion object {
	    @JvmStatic
		fun main(args: Array<String>) {
			// launch JavaFX application.
			launch(AnalysisApplication::class.java, *args)
		}
	}
}



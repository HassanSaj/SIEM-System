package com.analysis.util

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import org.springframework.stereotype.Component

import java.util.Optional

@Component
class AlertUtil {
    /**
     * Display an alert dialog given the parameters.
     * @param type Alert type.
     * @param title Alert title.
     * @param message Alert message.
     */
    fun showAlert(type: Alert.AlertType, title: String, message: String) {
        val alert = Alert(type)
        alert.headerText = null
        alert.contentText = message
        alert.title = title
        alert.show()
    }


    fun showAlertAndWait(type: Alert.AlertType, title: String, message: String): Optional<ButtonType> {
        val alert = Alert(type)
        alert.headerText = null
        alert.contentText = message
        alert.title = title
        return alert.showAndWait()
    }

    /**
     * Show success alert.
     * @param message Success message.
     */
    fun showSuccessAlert(message: String) {
        this.showAlert(Alert.AlertType.INFORMATION, "Success", message)
    }

    /**
     * Show error alert.
     * @param message Error message.
     */
    fun showErrorAlert(message: String) {
        this.showAlert(Alert.AlertType.ERROR, "Error", message)
    }
}

package com.analysis.util


/**
 * Class to represent a CSV file delimiter.
 * @param name The name of the character.
 * @param delimiterChar The character itself.
 */
class Delimiter(private val name: String, val delimiterChar: Char) {

    /**
     * @return Convert this file delimiter to string.
     */
    override fun toString(): String {
        return this.name.toUpperCase() + "[" + this.delimiterChar + "]"
    }
}

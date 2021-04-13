package com.datascrip.wms.core.util

class Validator {
}

object RegexPatterns {
    private const val REGEX_ALPHANUMERIC = "[A-Za-z0-9]+"
    private const val REGEX_NUMERIC = "[0-9]+"
    private const val REGEX_NUMERIC_WITH_PLUS = "[0-9+]+"
    private const val REGEX_NUMERIC_WITH_DOT = "[0-9.]+"
    private const val REGEX_ALPHABET = "[A-Za-z]+"
    private const val REGEX_ALPHANUMERIC_WITH_SPACE = "[A-Za-z0-9 ]+"
    private const val REGEX_ALPHABET_WITH_SPACE = "[A-Za-z ]+"
    private const val REGEX_ONE_TEXT_FIELD = "[A-Za-z 0-9,.:;'+()?/\\-]+"
    private const val REGEX_USERNAME = "(\\d.*){4}"
    private const val REGEX_ALLOWED_CHAR_NAME = "[0-9 A-Z a-z ,.:;?-]*"

    private const val REGEX_PRICE_GROUP = "^\\d+(.\\d{3})*\$"
    private const val REGEX_CELLPHONE_NUMBER = "^(^08)(\\d{8,11})\$"

    val AlphaNumeric = REGEX_ALPHANUMERIC.toRegex()
    val AlphaNumericWithSpace = REGEX_ALPHANUMERIC_WITH_SPACE.toRegex()
    val Alphabet = REGEX_ALPHABET.toRegex()
    val AlphabetWithSpace = REGEX_ALPHABET_WITH_SPACE.toRegex()
    val Numeric = REGEX_NUMERIC.toRegex()
    val NumericWithDot = REGEX_NUMERIC_WITH_DOT.toRegex()
    val NumericWithPlus = REGEX_NUMERIC_WITH_PLUS.toRegex()
    val Cellphone = REGEX_CELLPHONE_NUMBER.toRegex()
    val PriceWithThousandGrouping = REGEX_PRICE_GROUP.toRegex()
    val UserName = REGEX_USERNAME.toRegex()
    val OneTextFieldChar = REGEX_ONE_TEXT_FIELD.toRegex()
    val AllowedCharName = REGEX_ALLOWED_CHAR_NAME.toRegex()
}
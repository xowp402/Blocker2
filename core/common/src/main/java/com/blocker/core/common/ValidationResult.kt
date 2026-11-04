package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * نتیجه اعتبارسنجی یک ورودی در برنامه Blocker.
 */
@Serializable
data class ValidationResult(

    /**
     * آیا ورودی معتبر است؟
     */
    val isValid: Boolean,

    /**
     * کد خطای مربوط به ورودی.
     *
     * در صورت معتبر بودن ورودی، مقدار null است.
     */
    val errorCode: String? = null,

    /**
     * پیام قابل نمایش به کاربر.
     */
    val message: String? = null,

    /**
     * نام فیلدی که اعتبارسنجی روی آن انجام شده است.
     */
    val fieldName: String? = null
) {

    companion object {

        /**
         * ساخت نتیجه موفق.
         */
        fun success(
            fieldName: String? = null
        ): ValidationResult {
            return ValidationResult(
                isValid = true,
                fieldName = fieldName
            )
        }

        /**
         * ساخت نتیجه ناموفق.
         */
        fun failure(
            errorCode: String,
            message: String,
            fieldName: String? = null
        ): ValidationResult {
            return ValidationResult(
                isValid = false,
                errorCode = errorCode,
                message = message,
                fieldName = fieldName
            )
        }

        /**
         * خطای خالی بودن ورودی.
         */
        fun empty(
            fieldName: String? = null
        ): ValidationResult {
            return failure(
                errorCode = "EMPTY_INPUT",
                message = "این فیلد نمی‌تواند خالی باشد.",
                fieldName = fieldName
            )
        }

        /**
         * خطای تکراری بودن ورودی.
         */
        fun duplicate(
            fieldName: String? = null
        ): ValidationResult {
            return failure(
                errorCode = "DUPLICATE_INPUT",
                message = "این مورد قبلاً ثبت شده است.",
                fieldName = fieldName
            )
        }

        /**
         * خطای نامعتبر بودن قالب ورودی.
         */
        fun invalidFormat(
            fieldName: String? = null
        ): ValidationResult {
            return failure(
                errorCode = "INVALID_FORMAT",
                message = "قالب اطلاعات واردشده معتبر نیست.",
                fieldName = fieldName
            )
        }
    }
}

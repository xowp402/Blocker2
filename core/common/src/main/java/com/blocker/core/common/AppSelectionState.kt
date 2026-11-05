package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * وضعیت انتخاب برنامه‌ها در صفحه مدیریت برنامه‌ها.
 */
@Serializable
data class AppSelectionState(

    /**
     * شناسه بسته برنامه‌های انتخاب‌شده.
     */
    val selectedPackages: Set<String> = emptySet(),

    /**
     * آیا حالت انتخاب چندگانه فعال است؟
     */
    val isSelectionMode: Boolean = false,

    /**
     * آیا انتخاب همه برنامه‌های قابل‌نمایش فعال است؟
     */
    val isSelectAllActive: Boolean = false
) {

    /**
     * تعداد برنامه‌های انتخاب‌شده.
     */
    val selectedCount: Int
        get() = selectedPackages.size

    /**
     * بررسی انتخاب یک برنامه.
     */
    fun isSelected(packageName: String): Boolean {
        return packageName in selectedPackages
    }

    /**
     * انتخاب یک برنامه.
     */
    fun select(packageName: String): AppSelectionState {
        val normalized = packageName.trim()

        if (!AppPackageValidator.isValid(normalized)) {
            return this
        }

        return copy(
            selectedPackages = selectedPackages + normalized,
            isSelectionMode = true,
            isSelectAllActive = false
        )
    }

    /**
     * لغو انتخاب یک برنامه.
     */
    fun deselect(packageName: String): AppSelectionState {
        val updated = selectedPackages - packageName

        return copy(
            selectedPackages = updated,
            isSelectionMode = updated.isNotEmpty(),
            isSelectAllActive = false
        )
    }

    /**
     * انتخاب یا لغو انتخاب یک برنامه.
     */
    fun toggle(packageName: String): AppSelectionState {
        return if (isSelected(packageName)) {
            deselect(packageName)
        } else {
            select(packageName)
        }
    }

    /**
     * انتخاب تمام برنامه‌های ارائه‌شده.
     */
    fun selectAll(packageNames: Collection<String>): AppSelectionState {
        val validPackages = packageNames
            .map { it.trim() }
            .filter { AppPackageValidator.isValid(it) }
            .toSet()

        return copy(
            selectedPackages = validPackages,
            isSelectionMode = validPackages.isNotEmpty(),
            isSelectAllActive = validPackages.isNotEmpty()
        )
    }

    /**
     * لغو تمام انتخاب‌ها.
     */
    fun clear(): AppSelectionState {
        return AppSelectionState()
    }

    /**
     * حذف برنامه‌هایی که دیگر در فهرست موجود نیستند.
     */
    fun retainExisting(
        existingPackages: Collection<String>
    ): AppSelectionState {
        val existing = existingPackages.toSet()
        val retained = selectedPackages.intersect(existing)

        return copy(
            selectedPackages = retained,
            isSelectionMode = retained.isNotEmpty(),
            isSelectAllActive = false
        )
    }

    /**
     * بررسی اعتبار وضعیت انتخاب.
     */
    fun isValid(): Boolean {
        return selectedPackages.all {
            AppPackageValidator.isValid(it)
        } && (
            isSelectionMode || selectedPackages.isEmpty()
        ) && (
            isSelectAllActive || selectedPackages.isEmpty()
        )
    }
}

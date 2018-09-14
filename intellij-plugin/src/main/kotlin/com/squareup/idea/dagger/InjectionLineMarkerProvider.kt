package com.squareup.idea.dagger

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod

class InjectionLineMarkerProvider : LineMarkerProvider {
  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
    if (element is PsiMethod) {
      // TODO
    } else if (element is PsiField) {
      val fieldElement = element
    }
    return null
  }

  override fun collectSlowLineMarkers(
    elements: MutableList<PsiElement>,
    result: MutableCollection<LineMarkerInfo<PsiElement>>
  ) {
    // do nothing
  }

  companion object {
    private val ICON = IconLoader.getIcon("/icons/inject.png")
  }
}

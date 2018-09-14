package com.squareup.idea.dagger

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.uast.UField
import org.jetbrains.uast.getUastParentOfType
import org.jetbrains.uast.toUElement

class InjectionLineMarkerProvider : LineMarkerProvider {
  init {
    println("AHHHHHHHHHHHHH")
  }

  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
    val uElement = element.toUElement()
    if (uElement is UField) {
      println("sup")
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

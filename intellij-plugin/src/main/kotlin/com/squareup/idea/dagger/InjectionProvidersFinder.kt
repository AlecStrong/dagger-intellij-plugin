package com.squareup.idea.dagger

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiType
import org.jetbrains.kotlin.idea.refactoring.toPsiFile
import org.jetbrains.uast.UFile
import org.jetbrains.uast.toUElementOfType

class InjectionProvidersFinder {
  fun findProvidesMethods(project: Project, type: PsiType): List<NavigatablePsiElement> {
    val providesMethods = mutableListOf<NavigatablePsiElement>()
    ProjectFileIndex.getInstance(project).iterateContent { vFile ->
      val psiFile = vFile.toPsiFile(project)
      val uFile = psiFile?.toUElementOfType<UFile>()
          ?: return@iterateContent true
      uFile.classes
          .filter { it.findAnnotation("dagger.Module") != null }
          .flatMap { it.methods.toList() }
          .forEach { method ->
            if (method.returnType == type && method.findAnnotation("dagger.Provides") != null) {
              providesMethods += method
            }
          }
      return@iterateContent true
    }
    return providesMethods
  }
}

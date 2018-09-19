package com.squareup.idea.dagger.index

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class InjectionSitesIndexExtensionTest {
  @Test fun kotlinFieldOneLineImported() {
    val content = """
      |import android.view.EditText
      |import javax.inject.Inject
      |
      |class MyClass {
      |  @Inject lateinit var editText: EditText
      |}
      |""".trimMargin()
    val sites = TextBasedDaggerSitesLocator.findInjectionSites("MyClass.kt", content)
    assertThat(sites).hasSize(1)
    assertThat(sites).containsEntry("android.view.EditText", listOf("MyClass.kt" to 73))
  }

  @Test fun kotlinFieldOneLineNotImported() {
    val content = """
      |import javax.inject.Inject
      |
      |class MyClass {
      |  @Inject lateinit var editText: android.view.EditText
      |}
      |""".trimMargin()
    val sites = TextBasedDaggerSitesLocator.findInjectionSites("MyClass.kt", content)
    assertThat(sites).hasSize(1)
    assertThat(sites).containsEntry("android.view.EditText", listOf("MyClass.kt" to 44))
  }

  @Test fun kotlinFieldTwoLines() {
    val content = """
      |import android.view.EditText
      |import javax.inject.Inject
      |
      |class MyClass {
      |  @Inject
      |  lateinit var editText: EditText
      |}
      |""".trimMargin()
    val sites = TextBasedDaggerSitesLocator.findInjectionSites("MyClass.kt", content)
    assertThat(sites).hasSize(1)
    assertThat(sites).containsEntry("android.view.EditText", listOf("MyClass.kt" to 73))
  }

  @Test fun javaFieldOneLineImported() {
    val content = """
      |import android.view.EditText;
      |import javax.inject.Inject;
      |
      |public class MyClass {
      |  @Inject EditText editText;
      |}
      |""".trimMargin()
    val sites = TextBasedDaggerSitesLocator.findInjectionSites("MyClass.java", content)
    assertThat(sites).hasSize(1)
    assertThat(sites).containsEntry("android.view.EditText", listOf("MyClass.java" to 82))
  }

  @Test fun javaFieldOneLineNotImported() {
    val content = """
      |import javax.inject.Inject;
      |
      |public class MyClass {
      |  @Inject android.view.EditText editText;
      |}
      |""".trimMargin()
    val sites = TextBasedDaggerSitesLocator.findInjectionSites("MyClass.java", content)
    assertThat(sites).hasSize(1)
    assertThat(sites).containsEntry("android.view.EditText", listOf("MyClass.java" to 52))
  }

  @Test fun javaFieldTwoLines() {
    val content = """
      |import android.view.EditText;
      |import javax.inject.Inject;
      |
      |public class MyClass {
      |  @Inject EditText editText;
      |}
      |""".trimMargin()
    val sites = TextBasedDaggerSitesLocator.findInjectionSites("MyClass.kt", content)
    assertThat(sites).hasSize(1)
    assertThat(sites).containsEntry("android.view.EditText", listOf("MyClass.kt" to 82))
  }
}

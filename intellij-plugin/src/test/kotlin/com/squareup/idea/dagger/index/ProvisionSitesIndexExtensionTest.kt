package com.squareup.idea.dagger.index

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ProvisionSitesIndexExtensionTest {
  @Test fun kotlinOneLineImported() {
    val content = """
      |import android.view.EditText
      |import dagger.Provides
      |
      |class MyModule {
      |  @Provides fun provideEditText(): EditText = EditText()
      |}
      |""".trimMargin()
    val sites = TextBasedDaggerSitesLocator.findProvisionSites("MyModule.kt", content)
    assertThat(sites).hasSize(1)
    assertThat(sites).containsEntry("android.view.EditText", listOf("MyModule.kt" to 70))
  }

  @Test fun kotlinOneLineNotImported() {
    val content = """
      |import dagger.Provides
      |
      |class MyModule {
      |  @Provides fun provideEditText(): android.view.EditText = android.view.EditText()
      |}
      |""".trimMargin()
    val sites = TextBasedDaggerSitesLocator.findProvisionSites("MyModule.kt", content)
    assertThat(sites).hasSize(1)
    assertThat(sites).containsEntry("android.view.EditText", listOf("MyModule.kt" to 41))
  }

  @Test fun kotlinTwoLines() {
    val content = """
      |import android.view.EditText
      |import dagger.Provides
      |
      |class MyModule {
      |  @Provides
      |  fun provideEditText(): EditText = EditText()
      |}
      |""".trimMargin()
    val sites = TextBasedDaggerSitesLocator.findProvisionSites("MyModule.kt", content)
    assertThat(sites).hasSize(1)
    assertThat(sites).containsEntry("android.view.EditText", listOf("MyModule.kt" to 70))
  }

  @Test fun javaOneLineImported() {
    val content = """
      |import android.view.EditText;
      |import dagger.Provides;
      |
      |public class MyModule {
      |  @Provides public EditText provideEditText() {
      |    return EditText();
      |  }
      |}
      |""".trimMargin()
    val sites = TextBasedDaggerSitesLocator.findProvisionSites("MyModule.java", content)
    assertThat(sites).hasSize(1)
    assertThat(sites).containsEntry("android.view.EditText", listOf("MyModule.java" to 79))
  }

  @Test fun javaOneLineNotImported() {
    val content = """
      |import dagger.Provides;
      |
      |public class MyModule {
      |  @Provides public android.view.EditText provideEditText() {
      |    return android.view.EditText();
      |  }
      |}
      |""".trimMargin()
    val sites = TextBasedDaggerSitesLocator.findProvisionSites("MyModule.java", content)
    assertThat(sites).hasSize(1)
    assertThat(sites).containsEntry("android.view.EditText", listOf("MyModule.java" to 49))
  }

  @Test fun javaTwoLines() {
    val content = """
      |import android.view.EditText;
      |import dagger.Provides;
      |
      |public class MyModule {
      |  @Provides
      |  public EditText provideEditText() {
      |    return EditText();
      |  }
      |}
      |""".trimMargin()
    val sites = TextBasedDaggerSitesLocator.findProvisionSites("MyModule.kt", content)
    assertThat(sites).hasSize(1)
    assertThat(sites).containsEntry("android.view.EditText", listOf("MyModule.kt" to 79))
  }
}

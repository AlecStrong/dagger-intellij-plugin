package com.squareup.idea.dagger.index

object TextBasedDaggerSitesLocator {
  fun findProvisionSites(fileName: String, content: String): Map<String, List<Site>> {
    val lines = content.lines()
    val imports = mutableSetOf<String>()
    val injectionSites = mutableMapOf<String, MutableList<Site>>()
    lines.forEachIndexed { index, line ->
      if (line.trim().startsWith("import")) {
        val end = if (line.endsWith(';')) line.length - 1 else line.length
        imports += line.substring(line.lastIndexOf(' ') + 1, end)
      } else if ("@Provides" in line || "@dagger.Provides" in line) {
        val injectedType = findTypeFromProvidesExpression(lines, index, imports).trim()
        injectionSites.putIfAbsent(injectedType, mutableListOf(fileName to index))
            ?.add(fileName to index)
      }
    }
    return injectionSites
  }

  private fun findTypeFromProvidesExpression(
    expressions: List<String>,
    index: Int,
    imports: Set<String>
  ): String {
    val expr = expressions[index]
    val tokens = expr.split(Regex("\\s+"))
    var isKotlinZone = false
    tokens.forEachIndexed { index, token ->
      when {
        token == "fun" -> isKotlinZone = true
        '(' in token && !isKotlinZone -> return tokens[index - 1].let { type ->
          imports.firstOrNull { it.endsWith(type) } ?: type
        }
        ')' in token && isKotlinZone -> return tokens[index + 1].let { type ->
          imports.firstOrNull { it.endsWith(type) } ?: type
        }
      }
    }
    return findTypeFromProvidesExpression(expressions, index + 1, imports)
  }

  fun findInjectionSites(fileName: String, content: String): Map<String, List<Site>> {
    val lines = content.lines()
    val imports = mutableSetOf<String>()
    val injectionSites = mutableMapOf<String, MutableList<Site>>()
    lines.forEachIndexed { index, line ->
      if (line.trim().startsWith("import")) {
        val end = if (line.endsWith(';')) line.length - 1 else line.length
        imports += line.substring(line.lastIndexOf(' ') + 1, end)
      } else if ("@Inject" in line || "@javax.inject.Inject" in line) {
        val injectedType = findTypeFromInjectExpression(lines, index, imports).trim()
        injectionSites.putIfAbsent(injectedType, mutableListOf(fileName to index))
            ?.add(fileName to index)
      }
    }
    return injectionSites
  }

  private fun findTypeFromInjectExpression(
    expressions: List<String>,
    index: Int,
    imports: Set<String>
  ): String {
    val expr = expressions[index]
    val tokens = expr.split(Regex("\\s+"))
    return when {
      // Java zone
      ';' in expr -> tokens[tokens.size - 2].let { type ->
        imports.firstOrNull { it.endsWith(type) } ?: type
      }
      // Kotlin zone
      ':' in expr -> tokens.last().let { type ->
        imports.firstOrNull { it.endsWith(type) } ?: type
      }
      // Probably next line has what we want
      else -> findTypeFromInjectExpression(expressions, index + 1, imports)
    }
  }
}

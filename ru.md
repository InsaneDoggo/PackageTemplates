---
layout: page
title: Package Templates
lang: ru
---

### Описание
Package Templates это плагин реализующий Multipe [File Templates][1]. Он позволяет создавать шаблоны, состоящие из директорий и File Tamplates.

### Подробнее:
{% assign listTutorials = site.tutorials | where_exp:"item", "item.lang == page.lang" %}
{% for tutorial in listTutorials %}
* [{{ tutorial.title }}]({{ site.baseurl }}{{ tutorial.url }}){% endfor %}

### Запланированные фичи
блабла..

### Awards
Понравился плагин? Ставь лойс: <a class="github-button" href="https://github.com/CeH9/PackageTemplates" data-icon="octicon-star" data-count-href="/CeH9/PackageTemplates/stargazers" data-count-api="/repos/CeH9/PackageTemplates#stargazers_count" data-count-aria-label="# stargazers on GitHub" aria-label="Star CeH9/PackageTemplates on GitHub">Star</a>

[1]: https://www.jetbrains.com/help/idea/2016.2/file-and-code-templates.html
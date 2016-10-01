---
layout: home
title: Package Templates
lang: ru
---

### Описание
Package Templates это плагин реализующий Multiple [File Templates][1]. Он позволяет создавать шаблоны, состоящие из директорий и File Tamplates.

### Подробнее:
{% assign listTutorials = site.tutorials | where_exp:"item", "item.lang == page.lang" | sort: 'order' %}
{% for tutorial in listTutorials %}
* [{{ tutorial.title }}]({{ site.baseurl }}{{ tutorial.url }}){% endfor %}

### Запланированные фичи
[Список][5] фич на *github*.<br>
Присылайте свои идеи (см. пункт *Предложения и Замечания*). Понравившиеся разработчику идеи появятся в этом списке и будут реализованы в новых версиях плагина.

### Предложения и Замечания

1. [Пост][4] на форуме. (Скоро появится)
2. [Почта][2] для вопросов связанных с плагином. В теме указать **"Package Templates"**
3. [Почта][3] для общих вопросов к разработчику.

### Awards
Понравился плагин? Ставь лойс: <a class="github-button" href="https://github.com/CeH9/PackageTemplates" data-icon="octicon-star" data-count-href="/CeH9/PackageTemplates/stargazers" data-count-api="/repos/CeH9/PackageTemplates#stargazers_count" data-count-aria-label="# stargazers on GitHub" aria-label="Star CeH9/PackageTemplates on GitHub">Star</a>

[1]: https://www.jetbrains.com/help/idea/2016.2/file-and-code-templates.html
[2]: {{ site.data.links.mailto_plugin }}
[3]: {{ site.data.links.mailto_developer }}
[4]: {{ site.data.links.forum_plugin }}
[5]: {{ site.data.links.github_todo_project }}
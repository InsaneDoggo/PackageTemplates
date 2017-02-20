---
layout: home
title: Package Templates
lang: ru
---

### Описание
Package Templates это плагин реализующий Multiple [File Templates][1]. Он позволяет создавать шаблоны, состоящие из директорий и File Tamplates.

### Поддерживаемые IDE
Плагин должен работать на всех **IntelliJ IDEA-based IDEs**, но тестировался только на следующих:

* Intellij Idea Community Edition.
* Android Studio.
* PyCharm.

### Подробнее:
{% assign listTutorials = site.tutorials | where_exp:"item", "item.lang == page.lang" | sort: 'order' %}
{% for tutorial in listTutorials %}
* [{{ tutorial.title }}]({{ site.baseurl }}{{ tutorial.url }}){% endfor %}

### Запланированные фичи
[Список][5] фич на *github*.<br>
Присылайте свои идеи (см. пункт *Предложения и Замечания*). Понравившиеся разработчику идеи появятся в этом списке и будут реализованы в новых версиях плагина.

### Контакты

1. [Почта][2] для вопросов связанных с плагином. В теме указать **"Package Templates"**
2. [Почта][3] для общих вопросов к разработчику.
3. Так же можно оставлять вопросы с помощью [Issues][6] на GitHub. С Label **question**

### Awards
Понравился плагин? Ставь лойс: <a class="github-button" href="https://github.com/CeH9/PackageTemplates" data-icon="octicon-star" data-count-href="/CeH9/PackageTemplates/stargazers" data-count-api="/repos/CeH9/PackageTemplates#stargazers_count" data-count-aria-label="# stargazers on GitHub" aria-label="Star CeH9/PackageTemplates on GitHub">Star</a>

[1]: https://www.jetbrains.com/help/idea/2016.2/file-and-code-templates.html
[2]: {{ site.data.links.mailto_plugin }}
[3]: {{ site.data.links.mailto_developer }}
[5]: {{ site.data.links.github_todo_project }}
[6]: {{ site.data.links.github_issues }}
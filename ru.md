---
layout: home
title: Package Templates
lang: ru
---

### Описание
Package Templates это плагин реализующий Multiple [File Templates][1]. Он позволяет создавать шаблоны, состоящие из директорий и File Tamplates.

### Подробнее:
{% assign listTutorials = site.tutorials | where_exp:"item", "item.lang == page.lang"%}
{% for nav_tutorial in site.data.navigation.tutorials %}{% for tutorial in listTutorials %}{% if nav_tutorial == tutorial.navigation %}
* [{{ tutorial.title }}]({{ site.baseurl }}{{ tutorial.url }}){% endif %}{% endfor %}{% endfor %}

### История изменений
[Изменения][8]

### Поддерживаемые IDE
Плагин должен работать на всех **IntelliJ IDEA-based IDEs**, но тестировался только на следующих:

* Intellij Idea Community Edition.
* Android Studio.
* PyCharm.

### Известные проблемы
* [Appcode Groups][7]

### Запланированные фичи
[Список][5] фич на *github*.<br>
Присылайте свои идеи (см. пункт *Предложения и Замечания*). Понравившиеся разработчику идеи появятся в этом списке и будут реализованы в новых версиях плагина.

### Контакты

1. [Почта][2] для вопросов связанных с плагином. В теме указать **"Package Templates"**
2. [Почта][3] для общих вопросов к разработчику.
3. Так же можно оставлять вопросы с помощью [Issues][6] на GitHub. С Label **question**

### Awards
Понравился плагин? Ставь лойс: <a class="github-button" href="https://github.com/CeH9/PackageTemplates" data-icon="octicon-star" data-count-href="/CeH9/PackageTemplates/stargazers" data-count-api="/repos/CeH9/PackageTemplates#stargazers_count" data-count-aria-label="# stargazers on GitHub" aria-label="Star CeH9/PackageTemplates on GitHub">Star</a>

[1]: {{ site.data.links.file_templates }}
[2]: {{ site.data.links.mailto_plugin }}
[3]: {{ site.data.links.mailto_developer }}
[5]: {{ site.data.links.github_todo_project }}
[6]: {{ site.data.links.github_issues }}
[7]: {{ site.baseurl}}{{ site.data.links.other_appcode_groups[page.lang]}}
[8]: {{ site.baseurl}}{{ site.data.links.other_changelog[page.lang]}}
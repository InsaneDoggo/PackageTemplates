---
layout: home
title: Package Templates
lang: en
---

**Note:** This version is translated from original ([Russian][6]). If you find some **errors** or **wrong language constructs** please contact with devs (You can find contact info below).

### About
Package Templates make Multiple [File Templates][1] possible. Plugin let you create **directories** and **File Templates** in one action.

### Tutorials:
{% assign listTutorials = site.tutorials | where_exp:"item", "item.lang == page.lang" | sort: 'order' %}
{% for tutorial in listTutorials %}
* [{{ tutorial.title }}]({{ site.baseurl }}{{ tutorial.url }}){% endfor %}

### Changelog
[Changelog][8]

### Supported IDE
Plugin must work with all **IntelliJ IDEA-based IDEs**, but tested with listed below only:

* Intellij Idea Community Edition.
* Android Studio.
* PyCharm.

### Known issues
* [Appcode Groups][7]

### Future features
[List][5] of features on *github*.<br>
Send us your ideas (see **Suggestions and comments**). Best ideas will be implemented in new version of the plugin.

### Contacts

1. [Email][2] for question about plugin. Set **"Package Templates"** as subject.
2. [Email][3] for private questions to developer.
3. Also feel free to use [Issues][4] on GitHub for your questions. With Label **question**

### Awards
Do you like this plugin and think it's useful? Then you could <a class="github-button" href="https://github.com/CeH9/PackageTemplates" data-icon="octicon-star" data-count-href="/CeH9/PackageTemplates/stargazers" data-count-api="/repos/CeH9/PackageTemplates#stargazers_count" data-count-aria-label="# stargazers on GitHub" aria-label="Star CeH9/PackageTemplates on GitHub">Star</a> this project.

[1]: https://www.jetbrains.com/help/idea/2016.2/file-and-code-templates.html
[2]: {{ site.data.links.mailto_plugin }}
[3]: {{ site.data.links.mailto_developer }}
[4]: {{ site.data.links.github_issues }}
[5]: {{ site.data.links.github_todo_project }}
[6]: {{site.baseurl}}/ru
[7]: {{ site.baseurl}}{{ site.data.links.other_appcode_groups[page.lang]}}
[8]: {{ site.baseurl}}{{ site.data.links.other_changelog[page.lang]}}
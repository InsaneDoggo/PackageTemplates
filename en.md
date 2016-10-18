---
layout: home
title: Package Templates
lang: en
---

**Note:** This version is translated from original ([Russian][6]). If you find some **errors** or **wrong language constructs** please contact with devs (You can find contact info below).

<hr>

### About
Package Templates make Multiple [File Templates][1] possible. Plugin let you create **directories** and **File Templates** in one action.

### Tutorials:
{% assign listTutorials = site.tutorials | where_exp:"item", "item.lang == page.lang" | sort: 'order' %}
{% for tutorial in listTutorials %}
* [{{ tutorial.title }}]({{ site.baseurl }}{{ tutorial.url }}){% endfor %}

### Future features
[List][5] of features on *github*.<br>
Send us your ideas (see **Suggestions and comments**). Best ideas will be implemented in new version of the plugin.

### Contacts

1. [Post][4] on forum. (Available soon)
2. [Email][2] for question about plugin. Set **"Package Templates"** as subject.
3. [Email][3] for private questions to developer.

### Awards
Do you like this plugin and think it's useful? Then you could <a class="github-button" href="https://github.com/CeH9/PackageTemplates" data-icon="octicon-star" data-count-href="/CeH9/PackageTemplates/stargazers" data-count-api="/repos/CeH9/PackageTemplates#stargazers_count" data-count-aria-label="# stargazers on GitHub" aria-label="Star CeH9/PackageTemplates on GitHub">Star</a> this project.

[1]: https://www.jetbrains.com/help/idea/2016.2/file-and-code-templates.html
[2]: {{ site.data.links.mailto_plugin }}
[3]: {{ site.data.links.mailto_developer }}
[4]: {{ site.data.links.forum_plugin }}
[5]: {{ site.data.links.github_todo_project }}
[6]: {{site.baseurl}}/ru
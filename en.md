---
layout: page
title: Package Templates
lang: en
---
<p>Documentation still in development!</p>

### About
Package Templates is extension for [File Templates][1]. Plugin let you create the folder that already contain File Tamplates in one action! Note: Folder can be disabled in settings if you need files only.

### Tutorials
{% assign listTutorials = site.tutorials | where_exp:"item", "item.lang == page.lang" %}
{% for tutorial in listTutorials %}
* [{{ tutorial.title }}]({{ site.baseurl }}{{ tutorial.url }}){% endfor %}
* File templates into folder
//todo tutorials..

### Awards
Do you like this plugin and think it's useful? Then you could <a class="github-button" href="https://github.com/CeH9/PackageTemplates" data-icon="octicon-star" data-count-href="/CeH9/PackageTemplates/stargazers" data-count-api="/repos/CeH9/PackageTemplates#stargazers_count" data-count-aria-label="# stargazers on GitHub" aria-label="Star CeH9/PackageTemplates on GitHub">Star</a> this project.

[1]: https://www.jetbrains.com/help/idea/2016.2/file-and-code-templates.html
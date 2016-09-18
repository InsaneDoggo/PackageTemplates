---
layout: page
---

### Описание
Package Templates это плагин основанный на [File Templates][1]. Он позволяет создавать шаблоны, состоящие из нескольких File Tamplates и папок, одним действием!

### Tutorials
{% assign listTutorials = site.tutorials %}
{% for tutorial in listTutorials %}    
* [{{ tutorial.title }}]({{ site.baseurl }}{{ tutorial.url }})
* {{ tutorial.url | inspect }}
{% endfor %}
* File templates into folder
//todo tutorials..


### Awards
Понравился плагин? Ставь лойс: <a class="github-button" href="https://github.com/CeH9/PackageTemplates" data-icon="octicon-star" data-count-href="/CeH9/PackageTemplates/stargazers" data-count-api="/repos/CeH9/PackageTemplates#stargazers_count" data-count-aria-label="# stargazers on GitHub" aria-label="Star CeH9/PackageTemplates on GitHub">Star</a> этому проекту.

[1]: https://www.jetbrains.com/help/idea/2016.2/file-and-code-templates.html


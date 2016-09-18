---
layout: page
---
<p>Documentation still in development!</p>

### About
Package Templates is extension for [File Templates][1]. Plugin let you create the folder that already contain File Tamplates in one action! Note: Folder can be disabled in settings if you need files only.

### Test
<p>{% assign listTutorials = site.tutorials %}
{% for tutorial in listTutorials %}
	<a href="{{ site.baseurl }}/pages/{{ page.name | replace: ".md", ""}}">{{ tutorial.title }}</a><br>
{% endfor %}</p>

### Tutorials
<ul class="posts">
	{% assign listFeatures = site.pages | where:"category", "feature" %}
	{% for page in listFeatures %}
		<li><a href="{{ site.baseurl }}/pages/{{ page.name | replace: ".md", ""}}">{{ page.title }}</a></li>
	{% endfor %}
</ul>
* File templates into folder
//todo tutorials..

### Awards
Do you like this plugin and think it's useful? Then you could <a class="github-button" href="https://github.com/CeH9/PackageTemplates" data-icon="octicon-star" data-count-href="/CeH9/PackageTemplates/stargazers" data-count-api="/repos/CeH9/PackageTemplates#stargazers_count" data-count-aria-label="# stargazers on GitHub" aria-label="Star CeH9/PackageTemplates on GitHub">Star</a> this project.

[1]: https://www.jetbrains.com/help/idea/2016.2/file-and-code-templates.html
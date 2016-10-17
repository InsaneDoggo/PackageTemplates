---
title: Errors
lang: en
order: 8
---

The plugin is flexible and provides a lot of options for customization. Security is price that you pay for this "freedom". E.g. file names are not imposed by restrictions and user can enter a file with an invalid name.

At this moment plugin is under **beta** and has a fairly primitive notification system of errors. Creating File wrapped in a "try ... catch" block (interception of exceptions in Java language) When an error occurred plugin will keep information about it and continue to work. When template finish work dialog with list of errors will be shown.

![failed_elements_dialog]({{ site.baseurl }}/images/tutorial/failed_elements_dialog.png){: .image}

As you can see in example above plugin got 2 errors:

1. Can't create file **7FooExampleBar**
2. Can't create file **Foo**

Use **{{ site.data.const.button_show_details }}** button to show full message. 

![failed_elements_dialog]({{ site.baseurl }}/images/tutorial/error_details.png){: .image}

Analyze the error messages and try to fix problem. If you think that the problem in the plugin, then contact with developer. You can find contact info [here][2].


[1]: {{ site.baseurl}}{{ site.data.links.tutorial_create_template[page.lang] }}
[2]: {{ site.baseurl}}/{{ page.lang }}
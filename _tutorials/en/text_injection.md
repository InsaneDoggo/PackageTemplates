---
title: Text Injection
lang: en
navigation: text_injection
---

Text Injection allow you to insert text to file.

![text_injection_dialog]({{ site.baseurl }}/images/tutorial/text_injection_dialog.png){: .image}

1. Short description.
2. [Custom Path][2]. Initial Directory = value of GlobalVariable **CTX_DIR_PATH** (Description of this Var [here][3])
3. Inject Direction.
4. Text to search.
5. RegExp. Select checkbox to use pattern with inputfield (Plugin uses default impl from standart JDK (Java), [details][1])
6. Text to insert (Global and Predefined vars available).

**Note:** Use **Enter** for newline ( **\n** doesn't works ).

### Inject Direction

![inject_direction_dropdown]({{ site.baseurl }}/images/tutorial/inject_direction_dropdown.png){: .imageFragment}

**Example:**

* **BEFORE** = `Before`
* **AFTER** = `After`
* **PREV_LINE** = `Prev Line`
* **NEXT_LINE** = `Next Line`
* **REPLACE** = `Replace`
* **START_OF_FILE** = `Start of File`
* **SOF_END_OF_LINE** = `Start of File, End of line`
* **END_OF_FILE** = `End of File`
* **EOF_START_OF_LINE** = `End of File, Start of line`

![injection_directions_example]({{ site.baseurl }}/images/tutorial/injection_directions_example.png){: .imageFragment}

### Regex Helper

You can use button(see picture above) to show dialog for testing your patterns:

![regexp_helper_dialog]({{ site.baseurl }}/images/tutorial/regexp_helper_dialog.png){: .image}

**Note:** Dialog isn't modal (Allow you to switch between dialog and parent window)

[1]: {{ site.data.links.regexp_docs }}
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_custom_path[page.lang] }}
[3]: {{ site.baseurl}}{{ site.data.links.tutorial_global_variables[page.lang] }}
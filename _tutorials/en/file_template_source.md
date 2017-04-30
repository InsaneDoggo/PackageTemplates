---
title: FileTemplate Source
lang: en
order: 2
---

You may have templates with same names but in different [schemes][1]. **FileTemplate Source** define source where to find templates.

![file_template_source_dropdown]({{ site.baseurl }}/images/tutorial/file_template_source_dropdown.png){: .imageFragment}

* DEFAULT_ONLY - in **default** dir only.
* PROJECT_ONLY - in **project** dir only.
* PROJECT_PRIORITY - in **project** dir, if not found then trying to find in **default** dir.
* DEFAULT_PRIORITY - in **default** dir, if not found then trying to find in **project** dir.

**Note:** All modes except **DEFAULT_ONLY** require switching to the **Project Scheme** for correct operation. Otherwise, you will see WarningDialog.

[1]: {{ site.baseurl}}{{ site.data.links.tutorial_scheme[page.lang] }}
---
title: Сочетания клавиш
lang: ru
navigation: shortcuts
---

Сочетания клавиш хорошо описаны в документации от **jetbrains:**<br>

* [Keymap; Actions][2]
* [Shortcuts; Searching for actions][1]

Плагин использует **Action** для запуска **Основного диалога**. Для каждого из шаблонов создается Action.(При условии, что отмечен соответствующий флажок, подробнее [здесь][4]).

В настройках пользуйтесь поиском по имени. Action **Основного диалога** зарегистрирован под именем **"{{ site.data.const.main_action_name }}"**. Actions шаблонов используют их имена.

### Контекст вызова
Где будет создан шаблон? Для успешного использования сочетаний клавиш **фокус** должен быть на:

1. **Файле** или **директории** в **Project View**. Если выбран файл, то шаблон будет создан в той же директории, если выбрана директория, то шаблон будет создан внутри нее.

![context_project_view]({{ site.baseurl }}/images/tutorial/context_project_view.png){: .imageFragment}

2. **Редакторе файла**. Шаблон будет создан в той же директории, что и редактируемый файл.

![context_file_editor]({{ site.baseurl }}/images/tutorial/context_file_editor.png){: .imageFragment}

**Примечание:** Actions шаблонов регистрируются при запуске IDE. Если вы изменяли **флажок регистрации Action** в настройках шаблона или создали шаблон с нуля, то не забудьте перезапустить IDE.


[1]: {{ site.data.links.shortcuts }}
[2]: {{ site.data.links.shortcuts_actions }}
[3]: {{ site.data.links.jetbrains }}
[4]: {{ site.baseurl}}{{ site.data.links.tutorial_create_template[page.lang] }}
---
title: Вставка текста (Text Injection)
lang: ru
navigation: text_injection
---

Text Injection позволяет вставить текст в существующий файл.

![text_injection_dialog]({{ site.baseurl }}/images/tutorial/text_injection_dialog.png){: .image}

1. Краткое описание.
2. [Custom Path][2]. Стартовая директория = глобальной переменной **CTX_DIR_PATH** (Описание этой переменной [здесь][3])
3. Inject Direction (Место вставки).
4. Искомый текст.
5. Регулярные выражения RegExp. Если отмечено, то вместо простого текста в 4-м пункте указывается Pattern(Используется реализация из стандартной библиотеки языка Java, [подробнее][1])
6. Текст, который будет вставлен(Можно использовать Глобальные и Стандартные переменные).

**Примечание:** Для переноса строки используйте Enter ( **\n** не сработает ).

### Inject Direction (Место вставки)

![inject_direction_dropdown]({{ site.baseurl }}/images/tutorial/inject_direction_dropdown.png){: .imageFragment}

**Пример:**

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

Можно вызвать(соответсвующей кнопкой) диалог в котором удобно тестировать шаблон:

![regexp_helper_dialog]({{ site.baseurl }}/images/tutorial/regexp_helper_dialog.png){: .image}

**Примечание:** Диалог не модальный (Можно переключаться между родительским окном и диалогом)

[1]: {{ site.data.links.regexp_docs }}
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_custom_path[page.lang] }}
[3]: {{ site.baseurl}}{{ site.data.links.tutorial_global_variables[page.lang] }}
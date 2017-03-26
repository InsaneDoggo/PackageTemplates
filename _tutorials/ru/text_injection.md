---
title: Вставка текста (Text Injection)
lang: ru
order: 7
---

Text Injection позволяет вставить текст в существующий файл.

![text_injection_dialog]({{ site.baseurl }}/images/tutorial/text_injection_dialog.png){: .image}

1. Краткое описание.
2. [Custom Path][2]
3. Inject Direction (Место вставки).
4. Искомый текст.
5. Регулярные выражения RegExp. Если отмечено, то вместо простого текста в 4-м пункте указывается Pattern(Используется реализация из стандартной библиотеки языка Java, [подробнее][1])
6. Текст, который будет вставлен(Можно использовать Глобальные и Стандартные переменные).

**Примечание:** Для переноса строки используйте Enter ( **\n** не сработает ).

### Inject Direction (Место вставки)

![inject_direction_dropdown]({{ site.baseurl }}/images/tutorial/inject_direction_dropdown.png){: .imageFragment}

Пример:

![injection_directions_example]({{ site.baseurl }}/images/tutorial/injection_directions_example.png){: .imageFragment}

### Regex Helper

Можно вызвать(соответсвующей кнопкой) диалог в котором удобно тестировать шаблон:

![regexp_helper_dialog]({{ site.baseurl }}/images/tutorial/regexp_helper_dialog.png){: .image}

**Примечание:** Диалог не модальный (Можно переключаться между родительским окном и диалогом)

[1]: {{ site.data.links.regexp_docs }}
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_custom_path[page.lang] }}
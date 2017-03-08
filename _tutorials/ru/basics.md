---
title: Основы
lang: ru
order: 2
---

Чтобы открыть **PackageTemplates** воспользуйтесь разделом **new** контекстного меню. Так же можно настроить [Сочетания клавиш][1].

![context_menu]({{ site.baseurl }}/images/tutorial/context_menu.png){: .imageFragment}


![main_dialog]({{ site.baseurl }}/images/tutorial/main_dialog.png){: .image}

1. Создать
2. Редактировать
3. В Добавить\Удалить в\из **{{ site.data.const.favourites }}**.
4. Настройки Плагина
5. Экспорт
6. Импорт

### Выбор шаблона
Есть два варианта выбора:

**А.** Указав путь к файлу (.json)<br>
**B.** Из **{{ site.data.const.favourites }}**<br>

**Примечание:** **{{ site.data.const.favourites }}** запоминает путь к файлу. Если при запуске IDE по сохраненному пути не будет файла, то шаблон автоматически удалится из списка **{{ site.data.const.favourites }}**. 

В **{{ site.data.const.favourites }}** можно добавлять несколько шаблонов.

[1]: {{ site.baseurl}}{{ site.data.links.tutorial_shortcuts[page.lang] }}
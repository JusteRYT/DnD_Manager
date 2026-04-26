# Architecture Skeleton (PR-02)

Дата: 2026-04-25.

## Введенные слои

1. `application`
- порты (`CharacterGateway`, `ScreenNavigator`)
- use-cases (`Save`, `Load`, `List`, `Delete`)
- composition root container (`AppContext`)

2. `infrastructure`
- JavaFX-адаптер навигации (`FxScreenNavigator`)

3. `ui` (текущие `screen/*`, `overview/*`)
- начали переводить экраны на use-case вызовы

## Текущее правило зависимости

- UI зависит от `application`.
- `application` зависит только от портов.
- `infrastructure` реализует порты.

## Что уже переведено

1. `MainApp` использует `AppContext` для сборки зависимостей.
2. `StorageService` переведен на constructor injection и реализует `CharacterGateway`.
3. `StartScreen` использует `StartScreenController` и `ScreenNavigator`.
4. `CharacterCreateScreen`, `CharacterEditScreen`, `CharacterSelectionScreen`, `CharacterOverviewScreen` вызывают `application.usecase.*`.
5. `TopBar` разгружен через `TopBarController` (действия/навигация/экспорт вынесены из view).
6. `CharacterImportExportScreen` переведен на use-case для `list/load` и убраны `printStackTrace`.
7. `AppButtonFactory` декомпозирован на специализированные реализации:
   - `GradientButtonFactory`
   - `OutlineButtonFactory`
   - `IconButtonFactory`
   Фасад `AppButtonFactory` сохранен для обратной совместимости.
8. HUD/диалоги (`HpBar`, `ManaBar`, `InspirationBox`, `EditStatsDialog`, `LevelUpDialog`) переведены на `SaveCharacterUseCase`.
9. `CharacterImportExportScreen` разгружен через `CharacterImportExportController`.
10. `CharacterSelectionScreen` разгружен через `CharacterSelectionController`.
11. `StartScreen` больше не содержит update-flow orchestration:
    логика проверки/применения обновлений вынесена в `StartScreenUpdateController`.
12. Прямые CRUD-вызовы `storageService.*` из UI/диалогов удалены:
    операции проходят через `application.usecase.*`.
13. Прямые вызовы `ScreenManager.setScreen(...)` в UI-слое убраны:
    переходы идут через `ScreenNavigator` / `FxScreenNavigator` адаптер.
14. `StartScreenController` переведен на dependency injection для transfer-service
    (без создания `CharacterTransferServiceImpl` внутри контроллера).
15. `AppContext` расширен: `CharacterTransferService` создается в composition root
    и передается в `StartScreen` из контекста.
16. `TopBar` декомпозирован на отдельные UI-компоненты:
    - `ActiveEffectsPane`
    - `AvatarClipboardPane`
17. Навигационный DI усилен:
    экраны поддерживают конструкторы с `ScreenNavigator`, а `StartScreenController`
    прокидывает один navigator вниз по flow вместо локального создания в каждом месте.
18. `StartScreen` переведен на явный dependency constructor:
    больше не пересоздает flow-зависимости при смене языка, а переиспользует
    `ScreenNavigator`/use-case/service через DI.
19. `CharacterOverviewScreen` поддерживает конструктор с `ScreenNavigator`:
    навигационный порт передается извне, а fallback-конструктор оставлен
    только для обратной совместимости.
20. Возвраты в `StartScreen` унифицированы:
    в контроллерах/экранах, где уже есть `ScreenNavigator`, используется
    конструктор `StartScreen(stage, storageService, screenNavigator)` без
    локального создания нового навигатора.
21. Контроллеры выбора/импорта персонажей разгружены от инфраструктуры:
    `CharacterSelectionController` и `CharacterImportExportController` больше
    не создают use-case внутри себя и не зависят от `Stage/StorageService`;
    зависимости и back-action передаются через конструктор.
22. В overview-слое убран локальный `SaveCharacterUseCase`-спам:
    `TopBarController`, `EditStatsDialog`, `LevelUpDialog`, `ResourcePanel`,
    `HpBar`, `ManaBar`, `InspirationBox` переведены на явную инъекцию одного
    `SaveCharacterUseCase` по цепочке из `CharacterOverviewScreen`.
23. `StartScreenController` теперь владеет `SaveCharacterUseCase` и
    прокидывает его в `CharacterCreateScreen` / `CharacterEditScreen`;
    формы больше не завязаны на локальное создание application-логики внутри flow.
24. `StartScreen` и `StartScreenController` переведены на `CharacterUseCases`
    как единый dependency object:
    экраны выбора/импорта получают готовые use-case из контроллера, а
    `CharacterImageIntegrityService` теперь может работать от `CharacterUseCases`.
25. `CharacterOverviewScreen` получил DI-конструктор с `SaveCharacterUseCase`,
    и flow `create/load` прокидывает use-case извне вместо локального создания.
26. `StartScreenController` стал owner для screen-контроллеров:
    `CharacterSelectionController` и `CharacterImportExportController`
    создаются на уровне orchestration, а соответствующие screens поддерживают
    конструкторы с готовым controller (thin-view подход).
27. `BuffsInventoryPanel`/`FamiliarsPanel` переведены на единый
    `SaveCharacterUseCase` из `CharacterOverviewScreen`:
    убрано локальное создание use-case внутри ветки overview-фамильяров.
28. Упрощен orchestration в `StartScreenController`:
    дублирующая логика сборки selection-flow вынесена в приватные helper-методы
    (`buildSelectionScreen`, `openStartScreen`) для более чистого SRP.
29. `TopBar` default-конструктор больше не создает `SaveCharacterUseCase`:
    используется уже собранный use-case из `CharacterOverviewScreen`.
30. Проверка целостности изображений переведена в idempotent режим:
    `CharacterImageIntegrityService.validateAndRepairAllCharactersOnce()`
    вызывается из `StartScreen` и не выполняет тяжелый проход повторно при
    возвратах в меню.
31. Вынесен `SaveCharacterUseCase` в `AbstractCharacterFormScreen`:
    `CharacterCreateScreen` и `CharacterEditScreen` больше не дублируют
    хранение/инициализацию use-case.
32. `AppContext` расширен `CharacterImageIntegrityService`, а `StartScreen`/
    `StartScreenController` получают его через DI для стабильного one-time
    запуска integrity-check без повторных полных проходов по персонажам.
33. Возврат из form/asset/topbar flow унифицирован через `Runnable` action:
    `AbstractCharacterFormScreen`, `AssetManagerScreen`, `TopBarController`
    поддерживают `backToStartAction` и меньше зависят от ручной сборки
    `StartScreen` внутри конкретных обработчиков.
34. Для picker-режима `AssetManagerScreen` добавлен отдельный конструктор без
    `StorageService`, а `IconButtonFactory` перестал передавать `null` в
    screen-конструктор.
35. Добавлен `StartScreenNavigation` helper для типового back-flow:
    повторяющиеся лямбды с `new StartScreen(...).getView()` в form/asset/
    selection/import/topbar ветках заменяются централизованным action-builder.
36. `StartScreenNavigation` расширен rich-overload для orchestration-слоя:
    `StartScreenController` больше не собирает `StartScreen` вручную, а
    использует helper с уже готовыми зависимостями (`CharacterUseCases`,
    `CharacterImageIntegrityService`, `CharacterTransferService`).
37. Удалены fallback-конструкторы в form/overview экранах, которые создавали
    `SaveCharacterUseCase` внутри (`AbstractCharacterFormScreen`,
    `CharacterCreateScreen`, `CharacterEditScreen`, `CharacterOverviewScreen`):
    теперь main-flow опирается на явную инъекцию use-case.
38. `TopBar` очищен от конструкторов со скрытой сборкой зависимостей
    (`StorageService`/`FxScreenNavigator`) и работает как thin-view с
    переданными `ScreenNavigator` + `SaveCharacterUseCase` + `backToStartAction`.
39. `BuffsInventoryPanel` и `FamiliarsPanel` упрощены до DI-only сигнатур:
    исключено локальное создание `SaveCharacterUseCase`, уменьшена связность
    overview-компонентов с storage-слоем.
40. `CurrencyBox` и `ResourcePanel` переведены с `StorageService` на
    `SaveCharacterUseCase`: прямой `storageService.saveCharacter(...)` удален
    из overview UI, сохранение валюты выполняется через application use-case.
41. `CharacterSelectionScreen` очищен от legacy-конструкторов с
    `Stage/StorageService/use-case` сборкой: экран работает как thin-view с
    единственным входом через готовый `CharacterSelectionController`.
42. `CharacterImportExportScreen` очищен от legacy-конструкторов с локальным
    созданием controller/use-case: экран принимает готовый
    `CharacterImportExportController`.
43. Из form/overview flow убрана лишняя передача `StorageService`:
    `AbstractCharacterFormScreen`, `CharacterCreateScreen`,
    `CharacterEditScreen`, `CharacterOverviewScreen` теперь используют
    `ScreenNavigator + SaveCharacterUseCase + backToStartAction`.
44. `StartScreen` очищен от скрытой сборки зависимостей:
    удалены конструкторы, которые локально создавали `CharacterUseCases`,
    `CharacterImageIntegrityService` и `CharacterTransferServiceImpl`.
    Сборка этих зависимостей перенесена в orchestration helper.
45. `StartScreenNavigation.backToStartAction(...)` (базовый overload) теперь
    явно собирает dependency-chain (`CharacterUseCases` + transfer/integrity)
    и вызывает расширенный DI-overload без скрытой сборки внутри `StartScreen`.
46. Удален legacy-конструктор `CharacterImageIntegrityService(StorageService)`;
    сервис принимает `CharacterUseCases` как dependency object.
47. `StartScreenNavigation` упрощен до rich-DI API:
    удалены базовые overload-методы `backToStartAction/openStart` с неявной
    локальной сборкой зависимостей.
48. `AssetManagerScreen` очищен от неиспользуемых инфраструктурных параметров
    (`StorageService`, `ScreenNavigator`) и теперь работает через явный
    `backToStartAction` (manager mode) или `onAssetSelected` (picker mode).
49. `StartScreenController` переведен на готовый `openStartAction`:
    удалены поля/параметры `StorageService` и integrity-сервиса, связанные
    только с back-навигацией; контроллер больше не пересобирает стартовый
    экран сам и использует переданный action.
50. Введен `StartScreenFlowFactory` + `DefaultStartScreenFlowFactory`:
    создание feature-экранов (`create/edit/load/transfer/assets`) вынесено из
    `StartScreenController`, что уменьшает связность и усиливает DIP.
51. `StartScreenController` переведен на фабричный контракт:
    прямые `new Character*Screen(...)` и сборка selection/import controllers
    удалены; контроллер теперь оркестрирует через `ScreenNavigator` + factory.
52. `StartScreen` и `StartScreenNavigation` очищены от `StorageService` в
    публичных сигнатурах стартового flow: back-навигация работает через
    готовые DI-зависимости (`ScreenNavigator`, `CharacterUseCases`, services).
53. Удален неиспользуемый helper `StartScreenNavigation.openStart(...)`:
    навигация на стартовый экран унифицирована через `Runnable`
    `backToStartAction(...)` и не дублирует API.
54. UI-сборка стартового экрана вынесена из `StartScreen` в
    `StartScreenViewBuilder` + `StartScreenViewActions`:
    `StartScreen` теперь сфокусирован на orchestration/wiring, а не на
    создании всех JavaFX-нод и привязке каждой кнопки вручную.
55. Добавлены unit-тесты для `StartScreenController` без Mockito-inline
    (через fake-объекты), чтобы стабильно проверять фабричный flow и
    навигационную оркестрацию на Java 23.
56. В update-flow введен `UpdateService` + `DefaultUpdateService`:
    `StartScreenUpdateController` больше не создает `UpdateChecker`/
    `UpdateManager` внутри сценариев, а получает обновляющий сервис через DI.
57. Добавлены unit-тесты `CharacterUseCasesTest` для базового application
    контракта (`save/load/list/delete`) через fake gateway.
58. Вынесена асинхронная orchestration-логика update-flow в
    `UpdateFlowCoordinator` (`DefaultUpdateFlowCoordinator`) с абстракциями
    `AsyncRunner` и `UiDispatcher`; `StartScreenUpdateController` стал тонким
    UI-слоем без прямого `Thread`/`Platform.runLater`.
59. Добавлены unit-тесты `DefaultUpdateFlowCoordinatorTest` на сценарии
    check/apply (success/error) без JavaFX/Mockito-inline.
60. `UpdateChecker` декомпозирован по SRP:
    получение релиза вынесено в `ReleaseProvider` (`GitHubApiReleaseProvider`),
    сравнение версий — в `VersionComparator` (`SemanticVersionComparator`).
61. `UpdateChecker` переведен на constructor injection (`ReleaseProvider`,
    `VersionComparator`, `currentVersionSupplier`) с сохранением default wiring
    для runtime-совместимости.
62. Обновлены и расширены updater-тесты:
    `AppUpdateTest` теперь проверяет реальный `SemanticVersionComparator`,
    добавлен `UpdateCheckerTest` для поведения check/fetch без сети.
63. Update-зависимости подняты в composition root (`AppContext`):
    `UpdateService` и `UpdateFlowCoordinator` создаются централизованно и
    прокидываются в `StartScreen`/`StartScreenUpdateController` через DI,
    без локального default-wiring внутри UI-контроллера.
64. `UpdateManager` декомпозирован на отдельные порты/адаптеры:
    `UpdatePackageDownloader`, `ApplicationDirectoryResolver`,
    `UpdatePackagePathProvider`, `UpdateInstallerLauncher`,
    `ApplicationTerminator`; runtime-дефолты вынесены в отдельные классы.
65. `UpdateManager` переведен на constructor injection с сохранением
    default-конструктора для обратной совместимости runtime.
66. Добавлены unit-тесты `UpdateManagerTest` для сценариев:
    отсутствие ZIP-ассета и успешный orchestration download/launch/terminate.
67. `AssetManagerScreen` декомпозирован:
    mode-flow вынесен в `AssetManagerController`,
    построение tab-pane/галереи и стили — в `AssetManagerTabPaneBuilder`.
    Экран оставлен тонким layout-контейнером.
68. Добавлены unit-тесты `AssetManagerControllerTest` для mode-логики
    (`manager/picker` labels и manager-exit action).
69. `AssetGalleryTab` разгружен по SRP:
    файловые операции и загрузка списка изображений вынесены в
    `AssetGalleryService` + `AssetGalleryController`, tab оставлен как view.
70. Добавлены unit-тесты `AssetGalleryServiceTest` на фильтрацию изображений
    и чтение файлов в режиме `ALL`.
71. `AssetActionHandler` разгружен:
    файловые rename/delete операции вынесены в `AssetFileService`,
    handler оставлен UI-координатором диалогов и refresh-flow.
72. Добавлены unit-тесты `AssetFileServiceTest` на rename/path/delete
    поведение.
73. Убраны hardcoded строки в assets UI-flow:
    `AssetActionHandler` и `AssetCard` переведены на i18n-ключи для rename/
    delete меню и confirm-сообщений (`messages_en/ru.properties`).
74. Удален static-антипаттерн в `GlobalAssetService`:
    сервис переведен на instance API c DI-friendly конструктором (root path),
    а вызовы в `AbstractEntityEditor` и inventory-диалогах переведены на
    внедряемый экземпляр сервиса.
75. Добавлен unit-тест `GlobalAssetServiceTest` на импорт файла в category
    директорию.
76. Вынесен общий выбор и импорт иконок предмета в отдельный
    `InventoryItemIconChooser`:
    `AddInventoryItemDialog` и `EditInventoryItemDialog` больше не содержат
    дублирующуюся `FileChooser`/import-логику и зависят от абстракции
    через внедряемый сервис.
77. `AddInventoryItemDialog` очищен от hardcoded UI-строк:
    заголовки, подписи, prompt-ы и названия sub-editor диалогов переведены
    на `I18n` ключи; также исправлена инициализация списков бафов/скиллов
    из `existingItem`, чтобы не терять уже прикрепленные эффекты в edit-flow.
78. Унифицированы подтверждающие/переименовывающие диалоги:
    `AppConfirmDialog`, `ConfirmDialog`, `FamiliarDialog`, `RenameDialog`
    переведены на i18n-ключи без зашитых английских/русских строк.
79. Добавлен пакет новых i18n-ключей (`messages_en/ru.properties`) для
    inventory-dialog flow (`dialog.inventory.*`) и общих confirm/rename
    сценариев (`button.confirm`, `dialog.renameAsset.*`,
    `dialog.familiar.editor.title`).
80. `FamiliarInfoDialog` дополнительно декомпозирован:
    построение header-блока (аватар, имя, мета) вынесено в отдельный
    `FamiliarHeaderBuilder`, а сам диалог оставлен координатором секций.
81. В `FamiliarInfoDialog` добавлен DI-friendly конструктор с
    внедряемым `FamiliarHeaderBuilder`, при этом публичный конструктор
    сохранен для обратной совместимости.
82. `InventoryItemIconChooser` переведен на DIP:
    выбор файла вынесен в контракт `ItemIconFilePicker` с JavaFX-адаптером
    `JavaFxItemIconFilePicker`, поэтому бизнес-логика импорта иконки больше
    не зависит напрямую от `FileChooser`.
83. Добавлены unit-тесты `InventoryItemIconChooserTest` для основных
    веток поведения (cancel/import success/import fail) без поднятия JavaFX UI.
84. `FamiliarSectionBuilder` переведен со static-утилиты на instance API:
    методы секций (`resources/stats/icon lists/lore`) стали объектными,
    что устраняет скрытую глобальность и упрощает дальнейшее внедрение
    специализированных форматтеров/стилей через DI.
85. `FamiliarInfoDialog` переведен на dependency injection для
    `FamiliarSectionBuilder` (помимо ранее вынесенного header-builder):
    диалог теперь оркестрирует two-builder flow без прямых static-вызовов.
86. `FullDescriptionDialog` декомпозирован:
    построение текстового блока вынесено в `DescriptionSectionBuilder`,
    а диалог оставлен контейнером scroll-layout и orchestration секций.
87. `FullDescriptionDialog` получил DI-friendly конструктор с
    внедряемым `DescriptionSectionBuilder` (публичный конструктор сохранен),
    а placeholder пустого текста переведен на i18n-ключ
    `dialogDescription.emptyValue`.
88. Консолидирован confirm-flow для удаления ассетов:
    `AssetActionHandler` переведен с `ConfirmDialog` на `AppConfirmDialog`,
    что устраняет дублирование двух схожих подтверждающих диалогов.
89. Удален дублирующий `ConfirmDialog` как лишний UI-слой после
    унификации confirm-сценариев в `AppConfirmDialog`.
90. В update-flow вынесено форматирование текста прогресса загрузки:
    добавлены `UpdateProgressTextFormatter` и
    `MegabytesProgressTextFormatter`; `StartScreenUpdateController`
    получил DI-friendly внедрение formatter-а и перестал собирать
    строку прогресса inline.
91. Добавлен unit-тест `MegabytesProgressTextFormatterTest`; учтена
    локаль-зависимая десятичная пунктуация (`,`/`.`) в проверке.
92. В assets-flow выделен отдельный компонент
    `AssetDeleteConfirmMessageFactory`:
    формирование текста confirm-удаления вынесено из `AssetActionHandler`,
    что уменьшило его ответственность до orchestration UI + file service.
93. `AssetActionHandler` переведен на DI для `AssetDeleteConfirmMessageFactory`
    (с сохранением default-конструкторов для runtime-совместимости).
94. Добавлен unit-тест `AssetDeleteConfirmMessageFactoryTest` на single/multiple
    сценарии текста подтверждения удаления.
95. В update-flow выделен `UpdateProgressCalculator`:
    расчет прогресса загрузки вынесен из `StartScreenUpdateController` в
    отдельный компонент с защитой от `totalBytes <= 0` и clamp-логикой [0..1].
96. `StartScreenUpdateController` переведен на DI для двух отдельных
    компонент progress-flow: `UpdateProgressTextFormatter` (текст) и
    `UpdateProgressCalculator` (число), что усилило SRP и тестируемость.
97. Добавлен unit-тест `UpdateProgressCalculatorTest` на boundary-сценарии
    (zero-total, ratio, overflow, negative downloaded).
98. `NotesService` переведен на DI-friendly путь резолва директории:
    добавлен конструктор с `Function<String, Path>`, что устраняет
    жесткую зависимость сервиса от static-вызовов `CharacterStoragePathResolver`.
99. `CharacterNotesDialog` переведен на внедрение `NotesService`
    через конструктор (с сохранением default-конструктора),
    что снижает связанность UI с файловым слоем.
100. Добавлен unit-тест `NotesServiceTest` на сценарии round-trip
     save/load и поведение при отсутствии файла заметок.
101. В assets-flow выделен `AssetBaseNameResolver`:
     извлечение базового имени файла вынесено из `AssetActionHandler.rename`
     в отдельный компонент с покрытием edge-case для hidden-файлов.
102. `AssetActionHandler` дополнительно переведен на DI для
     `AssetBaseNameResolver`; default wiring сохранен.
103. Добавлен unit-тест `AssetBaseNameResolverTest` на случаи:
     extension/no-extension/hidden-file.
104. В application-слой добавлен `UpdateCharacterStatsUseCase`:
     логика обновления HP/armor/mana/level и сохранения персонажа
     вынесена из `EditStatsDialog` в отдельный use-case.
105. В application-слой добавлен `LevelUpCharacterUseCase`:
     инкремент уровня и сохранение персонажа вынесены из `LevelUpDialog`.
106. `EditStatsDialog` и `LevelUpDialog` переведены на orchestration
     через новые use-case классы (с сохранением совместимых публичных
     конструкторов через default wiring от `SaveCharacterUseCase`).
107. Добавлены unit-тесты application-слоя:
     `UpdateCharacterStatsUseCaseTest`, `LevelUpCharacterUseCaseTest`;
     также расширен `CharacterUseCasesTest` проверкой доступности новых
     производных use-case из `CharacterUseCases`.
108. В updater-слой добавлен `UpdateProgressView` + `UpdateProgressPresenter`:
     orchestration расчета прогресса и форматирования текста отделена
     от `StartScreenUpdateController` и UI-диалога.
109. `StartScreenUpdateController` дополнительно разгружен:
     callback загрузки обновления теперь делегирует в
     `UpdateProgressPresenter`, без inline-склейки прогресса/строки.
110. `AppUpdateProgressDialog` переведен на контракт `UpdateProgressView`
     и очищен от hardcoded-строки статуса через i18n-ключ
     `update.progress.initial`.
111. Добавлен unit-тест `UpdateProgressPresenterTest` на проверку
     корректной подачи progress/message во view.
112. В screen-слой добавлены `UpdateCheckButtonView` и
     `JavaFxUpdateCheckButtonView`: JavaFX `Button` адаптирован к
     отдельному контракту, чтобы убрать UI-детали из update-контроллера.
113. Добавлен `UpdateCheckButtonPresenter`:
     управление состояниями кнопки (`checking`/`ready`) вынесено из
     `StartScreenUpdateController` в отдельный presenter-компонент.
114. `StartScreenUpdateController` переведен на orchestration через
     `UpdateCheckButtonPresenter` + `UpdateProgressPresenter`, без прямых
     `setDisable/setText` вызовов и без inline-логики состояний кнопки.
115. Добавлен unit-тест `UpdateCheckButtonPresenterTest`.
116. В repository-слой введен порт `CharacterPathProvider` и default-адаптер
     `DefaultCharacterPathProvider` для замены прямых static-вызовов
     `CharacterStoragePathResolver` в runtime-коде.
117. `JsonCharacterRepository` и `CharacterJsonStore` переведены на DI
     через `CharacterPathProvider`; default-конструктор сохранен для
     обратной совместимости.
118. `IconStorageService` переведен на `CharacterPathProvider` (DI-friendly
     конструктор + default wiring), убрана прямая static-зависимость.
119. `CharacterTransferServiceImpl` переведен на `CharacterPathProvider`
     (DI-friendly конструктор + default wiring), что позволяет тестировать
     импорт/экспорт без подмены глобальных system-properties.
120. Добавлены unit-тесты `IconStorageServiceTest` и
     `CharacterTransferServiceImplTest` на path-provider сценарии.
121. `CharacterImageIntegrityService` переведен на `CharacterPathProvider`
     (с сохранением default-constructor wiring через
     `DefaultCharacterPathProvider`) вместо прямого static-доступа к
     `CharacterStoragePathResolver`.
122. `AppContext` обновлен: один `CharacterPathProvider` инстанс теперь
     централизованно прокидывается в `CharacterTransferServiceImpl` и
     `CharacterImageIntegrityService`.
123. Добавлен unit-тест `CharacterImageIntegrityServiceTest`:
     проверяет repair invalid icon paths и восстановление baseline-иконок
     (`no_image.png`, `user.png`) через path-provider root.
124. `NotesService` документационно синхронизирован с DI-подходом:
     описание сервиса обновлено от static resolver к injected resolver model.
125. `JsonCharacterRepositoryTest` переведен с глобальной подмены
     `System.setProperty(user.home/os.name/user.dir)` на явный
     `CharacterPathProvider` test-double (DI sandbox root).
126. В `JsonCharacterRepositoryTest` убраны прямые обращения к
     `CharacterStoragePathResolver` в assert-части; проверки привязаны к
     injected path-provider контракту.
127. Вынесена доменная мутация `InventoryItem` в отдельный
     `InventoryItemMutationService`:
     создание нового item и обновление существующего с нормализацией icon-path
     и копированием attach-коллекций (`buffs/skills`).
128. `AddInventoryItemDialog` переведен на orchestration через
     `InventoryItemMutationService` (вместо inline-setter логики и отдельного
     `getInventoryItem` helper); сохранена обратная совместимость
     публичных конструкторов.
129. `EditInventoryItemDialog` переведен на `InventoryItemMutationService`:
     запись полей и icon fallback унифицированы с add-flow.
130. Добавлен unit-тест `InventoryItemMutationServiceTest` на create/apply
     сценарии, включая fallback icon-path и проверку защитного копирования
     списков вложенных эффектов/скиллов.
131. В topbar-flow введен порт выбора файла экспорта
     `CharacterDescriptionSaveChooser` и JavaFX-адаптер
     `JavaFxCharacterDescriptionSaveChooser`.
132. Экспорт описания персонажа вынесен из `TopBarController` в отдельный
     сервис `CharacterDescriptionFileExporter` (controller больше не
     содержит `FileChooser`/`PrintWriter` IO-детали).
133. Сохранение VRChat save-string вынесено в
     `CharacterSaveStringService` с оптимизацией:
     сохранение выполняется только при реальном изменении trimmed-значения.
134. `TopBar` дополнительно декомпозирован:
     сборка блока VRChat-поля вынесена в `TopBarVrcSavePaneBuilder`,
     установка tooltip-попапов кнопок — в `TopBarTooltipInstaller`.
135. `TopBarController` переведен на orchestration через
     `CharacterDescriptionFileExporter` + `CharacterSaveStringService`
     (с default wiring и DI-friendly package constructor).
136. Добавлены unit-тесты topbar-сервисов:
     `CharacterSaveStringServiceTest` и
     `CharacterDescriptionFileExporterTest`.

## Следующий шаг

- Вынести оставшуюся бизнес-логику из overview/dialogs в отдельные
  coordinator/service-компоненты (в первую очередь `FamiliarInfoDialog` и
  смежные builder-классы), затем покрыть их unit-тестами.

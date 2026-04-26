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

## Следующий шаг

- Сократить прямую зависимость UI-screen от `StorageService`:
  начать с form/overview экранов и перевести их на dependency-object/use-case API.

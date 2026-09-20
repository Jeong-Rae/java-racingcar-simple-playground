# 자동차 경주

자동차 이름과 시도 횟수를 입력받아 경주를 구성하고, 한 라운드씩 경주를 진행하도록 구현했습니다.

입력 계층은 문자열과 기본 타입만 반환하며 도메인 객체를 알지 못합니다. `RacingController`가 입력값을 도메인 객체로 변환하고 실행 순서를 조합합니다. 도메인은 경주의 생명주기, 참가 자동차의 현재 위치, 전진 판단과 우승자 판정을 관리합니다. 라운드별 결과 출력은 아직 연결하지 않았습니다.

## 패키지 구조

```text
src/main/java
├── Apllication.java
├── Common
│   ├── ConsoleReader.java
│   └── ConsoleWriter.java
├── Controller
│   └── RacingController.java
├── Domain
│   ├── AdvanceDecider.java
│   ├── Name.java
│   ├── Position.java
│   ├── Racing.java
│   ├── RacingCars.java
│   ├── RacingStatus.java
│   ├── RandomAdvanceDecider.java
│   └── Round.java
└── View
    └── RacingFormView.java
```

- `Common`: 콘솔 입출력처럼 애플리케이션 전반에서 사용하는 공통 기능
- `View`: 자동차 이름과 시도 횟수를 입력받아 `List<String>`과 `int`로 반환
- `Controller`: View의 입력을 도메인 객체로 변환하고 경주 실행 순서를 조합
- `Domain`: 참가 상태, 위치, 라운드, 전진 판단, 경주 생명주기와 우승자 판정
- `Apllication`: 실제 콘솔 입출력기와 전진 판단 정책, Controller를 생성해 실행

## 전체 흐름

1. `Apllication`이 `ConsoleReader`, `ConsoleWriter`, `RacingFormView`, `RandomAdvanceDecider`, `RacingController`를 생성합니다.
2. `RacingFormView`가 자동차 이름을 `List<String>`으로, 시도 횟수를 `int`로 반환합니다.
3. `RacingController`가 이름을 `Name`으로 변환하고 `RacingCars`를 구성합니다.
4. `Racing.ready()`가 참가 자동차, 전체 `Round`, 하나의 `AdvanceDecider`를 받아 `READY` 상태의 경주를 생성합니다.
5. `start()`를 호출하면 상태가 `RACING`으로 전환됩니다.
6. `advance()`를 한 번 호출할 때마다 정확히 한 라운드가 진행됩니다. `RacingCars`는 같은 `AdvanceDecider`를 모든 참가 자동차에 순서대로 적용합니다.
7. 마지막 라운드가 끝나면 `Racing`이 즉시 `FINISHED` 상태로 전환됩니다.
8. `positions()`로 현재 참가자별 위치를 조회할 수 있습니다. `winners()`는 `FINISHED` 상태에서만 호출할 수 있습니다.

View는 `Name`, `Position`, `Round` 같은 도메인 타입을 참조하지 않습니다. 현재 위치를 라운드마다 출력할 때는 `advance()` 직후 `positions()`를 조회해 출력 계층으로 변환할 수 있습니다.

## 도메인 객체별 책임과 역할

| 객체 | 책임과 역할 |
| --- | --- |
| `Racing` | 경주의 `READY → RACING → FINISHED` 생명주기, 전체 라운드, 완료한 라운드 수와 전진 판단 정책을 관리합니다. `advance()` 한 번은 한 라운드를 의미합니다. |
| `RacingStatus` | 경주의 `READY`, `RACING`, `FINISHED` 상태를 표현합니다. |
| `RacingCars` | 참가 자동차의 `Name → Position` 관계를 `LinkedHashMap`으로 관리합니다. 참가 순서 유지, 중복 이름 검증, 한 라운드 전진, 현재 위치 조회와 선두 판정을 담당합니다. |
| `Name` | 참가 자동차를 식별하는 이름을 표현합니다. 공백 여부, 최대 길이와 허용 문자를 검증합니다. |
| `Position` | 현재 위치를 표현합니다. 시작 위치와 한 칸 전진, 위치 비교를 담당합니다. |
| `Round` | 전체 경주 횟수를 표현하고 허용 범위를 보장합니다. |
| `AdvanceDecider` | 한 자동차가 현재 라운드에서 전진하는지를 판단하는 정책의 계약입니다. |
| `RandomAdvanceDecider` | 0부터 9까지의 난수 중 값이 4 이상일 때 전진하도록 판단합니다. |

기존 `Race`와 `RacingCar`는 제거했습니다. 현재 요구사항에서 자동차별로 독립된 정책이나 별도의 행동을 관리하지 않기 때문에, 경주 자체의 생명주기는 `Racing`으로 이동하고 참가자의 이름과 위치 관계는 `RacingCars`가 직접 관리합니다.

`AdvanceDecider`는 `Racing`이 하나만 보유합니다. 자동차별 전략을 제공하기 위한 목적보다 난수라는 외부 불확실성을 경주 진행 로직에서 분리하고 테스트를 결정적으로 수행하기 위한 정책 객체로 사용합니다.

### 추가로 정의한 도메인 규칙

요구사항에 직접 명시되지 않았지만 도메인 경계를 분명히 하기 위해 다음 규칙을 적용합니다.

| 규칙 | 이유 |
| --- | --- |
| 자동차 이름은 빈 문자열이나 공백만으로 구성할 수 없습니다. | 자동차를 식별할 수 없는 이름 생성을 막습니다. |
| 자동차 이름에는 한글과 영문만 사용할 수 있습니다. | 이름에 허용되는 문자 범위를 명확히 합니다. |
| 경주에는 자동차가 1대 이상 필요합니다. | 참가 자동차가 없으면 진행과 선두 판정이 성립하지 않습니다. |
| 같은 경주에서 자동차 이름은 중복될 수 없습니다. | `Name`이 참가자를 식별하는 키이므로 동일한 참가자를 구분할 수 없는 상태를 막습니다. |
| 시도 횟수는 1회 이상 100회 이하입니다. | 0회 경주를 제외하고 실행 범위의 상한을 명확히 합니다. |
| 자동차 위치는 0부터 100까지입니다. | 시작 위치가 0이고 최대 시도 횟수가 100회이므로 실제 도달 범위와 일치시킵니다. |
| `READY` 상태에서만 경주를 시작할 수 있습니다. | 동일한 경주를 중복 시작하는 호출을 방지합니다. |
| `RACING` 상태에서만 한 라운드를 진행할 수 있습니다. | 시작 전 또는 종료 후에 위치가 변경되는 것을 방지합니다. |
| 우승자는 `FINISHED` 상태에서만 조회할 수 있습니다. | 진행 중인 선두와 최종 우승자를 구분합니다. |

## 테스트 수행 방식

테스트는 JUnit 5와 AssertJ를 사용합니다.

```bash
./gradlew test
```

테스트 코드는 준비(Arrange), 실행(Act), 검증(Assert)이 구분되도록 작성했습니다. 입력과 기대값을 먼저 준비하고, 실행 결과는 `actual...` 변수에 저장한 뒤 검증합니다.

경계가 있는 값은 경계값과 바로 인접한 값을 중심으로 검증합니다.

- `Name`: 최대 길이 5자, 길이 초과, 공백, 허용되지 않은 문자
- `Position`: 0, 1, 99, 100과 범위를 벗어난 -1, 101
- `Round`: 1, 2, 99, 100과 범위를 벗어난 0, 101
- `RandomAdvanceDecider`: 전진 기준값을 중심으로 3, 4, 5

`RacingCarsTest`는 참가 순서, 시작 위치, 중복 이름, 하나의 정책을 참가자에게 순서대로 적용하는 동작, 위치 스냅샷과 공동 선두를 검증합니다.

`RacingTest`는 `READY → RACING → FINISHED` 상태 전이와 잘못된 호출 순서, 한 라운드 단위 진행, 100라운드 경계와 종료 후 우승자 조회를 검증합니다.

`RandomAdvanceDeciderTest`는 `FixedRandomGenerator`로 난수 값을 고정해 전진 기준을 검증합니다. `Racing`과 `RacingCars`는 `TestAdvanceDecider`에 정해진 판단 순서를 전달해 경주 결과를 재현합니다.

`ConsoleReader`와 `ConsoleWriter`는 자동차 경주와 관계없는 일반 문자열로 독립적으로 테스트합니다. `RacingFormView`는 입력값 변환과 입력 안내 출력을 검증합니다.

`RacingController`와 `Apllication`은 객체를 조합하고 실행을 연결하는 구성 코드로 두며 별도의 단위 테스트는 작성하지 않았습니다.

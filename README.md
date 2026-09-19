# 자동차 경주

자동차 이름과 시도 횟수를 입력받아 경주를 구성하고, 각 자동차가 전진 여부를 판단해 정해진 라운드만큼 경주를 진행하도록 구현했습니다.

입력 계층은 문자열과 기본 타입만 반환하며 도메인 객체를 알지 못합니다. `RacingController`가 입력값을 도메인 객체로 변환하고 경주 실행을 조합합니다. 경주 결과와 우승자 조회를 위한 도메인 로직까지 구현했으며, 결과 출력은 아직 연결하지 않았습니다.

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
│   ├── Race.java
│   ├── RacingCar.java
│   ├── RacingCars.java
│   ├── RandomAdvanceDecider.java
│   └── Round.java
└── View
    └── RacingFormView.java
```

- `Common`: 콘솔 입출력처럼 애플리케이션 전반에서 사용할 수 있는 공통 기능을 제공합니다.
- `View`: 자동차 이름과 시도 횟수를 입력받아 `List<String>`과 `int`로 반환합니다.
- `Controller`: View에서 받은 입력을 도메인 객체로 변환하고 경주 실행을 조합합니다.
- `Domain`: 자동차, 위치, 라운드, 전진 판단, 경주 진행과 우승자 판정 규칙을 담당합니다.
- `Apllication`: 실제 콘솔 입출력기, 전진 판단 정책, Controller를 생성해 애플리케이션을 시작합니다.

## 전체 흐름

1. `Apllication`이 `ConsoleReader`, `ConsoleWriter`, `RacingFormView`, `RandomAdvanceDecider`, `RacingController`를 생성합니다.
2. `RacingFormView`가 자동차 이름을 쉼표로 구분해 입력받고 `List<String>`으로 반환합니다. 시도 횟수는 `int`로 반환합니다.
3. `RacingController`가 각 이름을 `Name`과 `RacingCar`로 변환하고 `RacingCars`를 구성합니다. 모든 자동차는 하나의 `AdvanceDecider`를 공유합니다.
4. 시도 횟수는 `Round`로 변환합니다. `Race.run()`은 `Round` 값만큼 `RacingCars.advance()`를 호출합니다.
5. `RacingCars.advance()`는 모든 `RacingCar`에 전진을 요청합니다.
6. `RacingCar`는 `AdvanceDecider.shouldAdvance()` 결과가 `false`이면 현재 위치를 유지합니다. `true`이면 `Position.advance()`로 한 칸 전진합니다.
7. 경주가 끝난 뒤 `Race.winners()`를 호출하면 `RacingCars.leaders()`가 가장 앞선 자동차를 찾습니다. 같은 위치의 자동차가 여러 대면 모두 반환합니다.

View는 `Name`, `Round`, `RacingCar` 같은 도메인 타입을 참조하지 않습니다. 도메인 객체 생성과 실행 조합은 `RacingController`가 담당합니다.

## 도메인 객체별 책임과 역할

| 객체 | 책임과 역할 |
| --- | --- |
| `Name` | 자동차 이름을 표현합니다. 이름은 공백일 수 없고 5자 이하여야 하며 한글과 영문만 허용합니다. `Name.of()`로 생성합니다. |
| `Position` | 자동차 위치를 표현합니다. 위치는 0부터 100까지 허용합니다. 시작 위치는 `Position.ZERO`이며 `advance()`를 호출하면 한 칸 전진한 새 `Position`을 반환합니다. |
| `Round` | 경주 시도 횟수를 표현합니다. 1부터 100까지 허용하며 `Round.of()`로 생성합니다. |
| `AdvanceDecider` | 자동차가 전진할지 판단하는 정책의 계약을 정의합니다. `RacingCar`는 구체적인 랜덤 구현을 알지 않고 이 인터페이스에만 의존합니다. |
| `RandomAdvanceDecider` | 0부터 9까지의 난수를 만들고 값이 4 이상이면 전진하도록 판단합니다. |
| `RacingCar` | 자동차 이름과 현재 위치를 보유합니다. 전진 판단 결과가 `false`이면 즉시 반환하고, 전진할 수 있으면 위치를 한 칸 증가시킵니다. `isAt()`으로 특정 위치에 있는지도 판단합니다. |
| `RacingCars` | 여러 `RacingCar`를 하나의 컬렉션으로 관리합니다. 모든 자동차에 전진을 요청하고, 최대 위치를 기준으로 단독 또는 공동 선두를 찾습니다. 빈 자동차 목록은 허용하지 않습니다. |
| `Race` | 경주 진행을 담당합니다. 지정한 `Round`만큼 자동차들을 전진시키고, 경주가 끝난 뒤 우승자 조회를 `RacingCars`에 위임합니다. |

값 객체인 `Name`, `Position`, `Round`는 생성 시점에 자신의 유효 범위를 검사합니다. 도메인 규칙이 유효한 객체 내부에서 유지되도록 구성했습니다.

전진 판단은 `RacingCar`에서 분리했습니다. `RacingCar`는 전진 여부를 결정하는 방법보다 전진 결과에 따른 자신의 상태 변경에 집중합니다. 실제 실행에서는 `RandomAdvanceDecider`를 사용하고, 테스트에서는 결정된 값을 반환하는 테스트 대역을 사용합니다.

### 추가로 정의한 도메인 규칙

요구사항에 직접 명시되지 않았지만 도메인 경계를 분명히 하기 위해 다음 규칙을 추가했습니다.

| 규칙 | 이유 |
| --- | --- |
| 자동차 이름은 빈 문자열이나 공백만으로 구성할 수 없습니다. | 자동차를 식별할 수 없는 이름이 생성되는 것을 막기 위해 추가했습니다. |
| 자동차 이름에는 한글과 영문만 사용할 수 있습니다. | 숫자와 특수문자의 허용 여부가 모호하지 않도록 이름의 문자 범위를 명확히 했습니다. |
| 경주에는 자동차가 1대 이상 필요합니다. | 참가 자동차가 없으면 전진과 선두 판정의 의미가 없기 때문에 빈 자동차 목록을 허용하지 않습니다. |
| 시도 횟수는 1회 이상 100회 이하입니다. | 0회 경주를 제외하고 실행 횟수의 상한을 명확히 두기 위해 추가했습니다. |
| 자동차 위치는 0부터 100까지입니다. | 시작 위치가 0이고 최대 시도 횟수가 100회이므로 실제 경주에서 도달할 수 있는 범위와 일치시켰습니다. |

이 규칙들은 각 값을 소유한 `Name`, `Round`, `Position`, `RacingCars`가 직접 검증합니다.

## 테스트 수행 방식

테스트는 JUnit 5와 AssertJ를 사용합니다. 다음 명령으로 테스트를 실행할 수 있습니다.

```bash
./gradlew test
```

테스트 코드는 준비(Arrange), 실행(Act), 검증(Assert)이 구분되도록 작성했습니다. 준비 단계에서는 `input`, `expected...`와 테스트 대상을 구성하고, 실행 결과는 `actual...` 변수에 저장한 뒤 검증합니다.

단순한 숫자나 문자열 조합은 `@CsvSource`로 파라미터화했습니다. `List<Boolean>`처럼 구조가 있는 입력은 `@MethodSource`를 사용했습니다. 같은 동작을 여러 입력으로 확인할 때 테스트 로직을 반복하지 않고 입력과 기대 결과를 데이터로 분리했습니다.

경계가 있는 값은 경계값과 바로 인접한 값을 중심으로 검증했습니다.

- `Name`: 최대 길이 5자와 길이 초과, 공백, 허용되지 않은 문자
- `Position`: 0, 1, 99, 100과 범위를 벗어난 -1, 101
- `Round`: 1, 2, 99, 100과 범위를 벗어난 0, 101
- `RandomAdvanceDecider`: 전진 기준값을 중심으로 3, 4, 5

랜덤 동작은 테스트에서 실제 난수에 의존하지 않습니다. `FixedRandomGenerator`로 난수 값을 고정해 전진 기준을 검증합니다. `RacingCar`, `RacingCars`, `Race`는 `TestAdvanceDecider`에 `true`와 `false`의 순서를 전달해 경주 결과를 재현할 수 있도록 테스트합니다.

`ConsoleReader`와 `ConsoleWriter`는 자동차 경주와 관계없는 일반 문자열을 사용해 독립적으로 테스트합니다. `RacingFormView`는 문자열 입력을 기본 타입과 컬렉션으로 변환하는지, 입력 전에 안내 문구를 출력하는지를 검증합니다.

`RacingController`와 `Apllication`은 객체를 조합하고 실행을 연결하는 구성 코드로 두며 별도의 단위 테스트는 작성하지 않았습니다.

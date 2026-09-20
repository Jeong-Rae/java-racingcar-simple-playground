# 자동차 경주

자동차 이름과 시도 횟수를 입력받아 경주를 구성하고, 한 라운드씩 진행하는 자동차 경주 애플리케이션입니다.

입력 계층은 문자열과 기본 타입만 반환합니다. `RacingController`가 입력값을 도메인 객체로 변환하고 실행 순서를 조합하며, 도메인은 경주의 생명주기, 참가 자동차의 위치, 전진 판단과 우승자 판정을 관리합니다. 각 라운드가 끝나면 `RacingResultView`가 현재 라운드와 모든 참가 자동차의 위치를 출력하고, 경주가 끝나면 최종 우승자를 출력합니다.

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
    ├── RacingFormView.java
    └── RacingResultView.java
```

- `Common`: 콘솔 입출력처럼 애플리케이션 전반에서 사용하는 공통 기능
- `View`: 자동차 이름과 시도 횟수를 입력받고 라운드별 경주 결과를 출력
- `Controller`: 입력값을 도메인 객체로 변환하고 경주 실행 순서를 조합
- `Domain`: 참가 자동차, 위치, 라운드, 전진 판단, 경주 생명주기와 우승자 판정을 관리
- `Apllication`: 콘솔 입출력기, 전진 판단 정책과 Controller를 생성해 실행

## 실행 흐름

1. `Apllication`이 `ConsoleReader`, `ConsoleWriter`, `RacingFormView`, `RacingResultView`, `RandomAdvanceDecider`, `RacingController`를 생성합니다.
2. `RacingFormView`가 자동차 이름과 시도 횟수를 입력받습니다.
3. `RacingController`가 입력된 이름을 `Name` 목록으로 변환합니다.
4. `Racing.ready()`가 참가 자동차 이름 목록, 전체 `Round`, 하나의 `AdvanceDecider`를 받아 `RacingCars`를 내부에서 구성하고 `READY` 상태의 경주를 생성합니다.
5. `start()`를 호출하면 경주가 `RACING` 상태로 전환됩니다.
6. `advance()`를 한 번 호출할 때마다 한 라운드를 진행합니다. `RacingCars`는 같은 `AdvanceDecider`를 모든 참가 자동차에 순서대로 적용합니다.
7. 마지막 라운드가 끝나면 `Racing`이 `FINISHED` 상태로 전환됩니다.
8. `positions()`로 현재 위치를 조회하고, `FINISHED` 상태에서는 `winners()`로 최종 우승자를 조회할 수 있습니다.

View는 `Name`, `Position`, `Round` 같은 도메인 타입을 참조하지 않습니다. `RacingController`가 `advanceIfPossible()`로 라운드를 진행한 직후 `Racing`에 현재 위치를 질의하고, 이를 `Map<String, Integer>`로 변환해 `RacingResultView`에 전달합니다.

## 도메인 책임

| 객체 | 책임 |
| --- | --- |
| `Racing` | 경주의 Aggregate Root입니다. `READY → RACING → FINISHED` 생명주기, 전체 라운드, 완료한 라운드 수와 전진 판단 정책을 관리하고 경주 진행과 우승자 조회의 유일한 외부 진입점을 제공합니다. |
| `RacingStatus` | `Racing` 내부에서 사용하는 package-private 생명주기 상태입니다. `READY`, `RACING`, `FINISHED`를 표현합니다. |
| `RacingCars` | `Racing` 내부에서 사용하는 package-private 도메인 객체입니다. 참가 순서, `Name → Position` 관계, 실제 위치 변경과 선두 계산을 담당합니다. |
| `Name` | 자동차 이름의 공백 여부, 최대 길이와 허용 문자를 검증합니다. |
| `Position` | 시작 위치, 한 칸 전진과 위치 비교를 담당합니다. |
| `Round` | 전체 경주 횟수와 허용 범위를 표현합니다. |
| `AdvanceDecider` | 자동차의 전진 여부를 판단하는 정책 계약입니다. |
| `RandomAdvanceDecider` | 0부터 9까지의 난수 중 값이 4 이상일 때 전진하도록 판단합니다. |

`Racing`은 경주 Aggregate의 유일한 외부 진입점입니다. `RacingCars`는 package-private으로 제한하고 생성과 행동을 `Racing` 내부에서만 사용합니다. 따라서 경주 진행과 우승자 조회는 반드시 `Racing`의 생명주기 검사를 거칩니다. 자동차별 독립 정책이나 별도의 객체 상태가 필요하지 않으므로 기존 `Race`와 `RacingCar`는 사용하지 않습니다.

`AdvanceDecider`는 난수라는 외부 불확실성을 경주 진행 로직에서 분리합니다. `Racing`은 하나의 `AdvanceDecider`를 보유하고 각 라운드에서 모든 참가 자동차에 같은 정책을 적용합니다.

## 도메인 규칙

요구사항에 직접 명시되지 않은 규칙은 도메인 경계를 명확하게 유지하기 위해 추가했습니다.

| 규칙 | 이유 |
| --- | --- |
| 자동차 이름은 빈 문자열이나 공백만으로 구성할 수 없습니다. | 식별할 수 없는 이름 생성을 막습니다. |
| 자동차 이름에는 한글과 영문만 사용할 수 있습니다. | 허용되는 문자 범위를 명확히 합니다. |
| 경주에는 자동차가 1대 이상 필요합니다. | 참가 자동차가 없으면 경주와 선두 판정이 성립하지 않습니다. |
| 같은 경주에서 자동차 이름은 중복될 수 없습니다. | `Name`을 참가자 식별 키로 사용합니다. |
| 시도 횟수는 1회 이상 100회 이하입니다. | 0회 경주를 제외하고 실행 범위의 상한을 정의합니다. |
| 자동차 위치는 0부터 100까지입니다. | 시작 위치와 최대 시도 횟수에 따른 실제 도달 범위와 일치시킵니다. |
| `READY` 상태에서만 경주를 시작할 수 있습니다. | 동일한 경주의 중복 시작을 방지합니다. |
| `RACING` 상태에서만 한 라운드를 진행할 수 있습니다. | `advanceIfPossible()`이 상태를 내부에서 판단해 시작 전이나 종료 후의 위치 변경을 방지합니다. |
| 우승자는 `FINISHED` 상태에서만 조회할 수 있습니다. | 진행 중인 선두와 최종 우승자를 구분합니다. |

## 코드 작성 원칙

### 조건식

조건식은 리뷰어가 구현 세부를 직접 해석하지 않아도 코드의 판단 의도를 즉시 이해할 수 있도록 작성합니다. 조건식의 핵심 목적은 계산 과정을 노출하는 것이 아니라 판단의 의미를 드러내는 데 있습니다.

다음 기준을 적용합니다.

- `if`에서 사용하는 조건은 의미를 나타내는 `boolean` 지역 변수로 먼저 표현합니다.
- `boolean` 변수는 `is`, `has`, `can`, `should`와 같은 predicate 접두사를 사용해 판단 결과를 드러냅니다.
- 여러 조건은 의미가 구분되는 판단 단위로 나눈 뒤 관련 predicate 변수를 조합해 최종 조건을 표현합니다.
- 메서드 호출이나 스트림 연산처럼 구현 내용을 읽어야 의미를 알 수 있는 조건은 판단 목적을 나타내는 predicate 변수로 추상화합니다.
- 단순한 `null` 비교나 상태 비교처럼 표현 자체에서 의미가 충분히 드러나는 조건은 직접 사용할 수 있습니다.

### null 처리

필수 인자에 `null`이 전달되면 `NullPointerException`에 맡기지 않고 입력 오류를 명시적으로 검증합니다. 전달된 값이나 인자가 유효하지 않으면 `IllegalArgumentException`을 발생시키고, 현재 객체 상태에서 수행할 수 없는 동작이면 `IllegalStateException`을 발생시킵니다.

`null` 검증에는 `Objects.requireNonNull()`을 사용하지 않습니다. `Objects.isNull()`처럼 `NullPointerException`을 발생시키지 않는 보조 메서드는 사용할 수 있습니다. 각 객체는 자신의 생성 조건과 입력 계약을 직접 검사하고 문맥에 맞는 예외 메시지를 제공합니다.

## 테스트

테스트는 JUnit 5와 AssertJ를 사용합니다.

```bash
./gradlew test
```

테스트는 준비(Arrange), 실행(Act), 검증(Assert)이 구분되도록 작성합니다. 입력과 기대값을 먼저 준비하고, 실행 결과는 `actual...` 변수에 저장한 뒤 검증합니다.

경계가 있는 값은 경계값과 바로 인접한 값을 중심으로 검증합니다.

- `Name`: 최대 길이 5자, 길이 초과, 공백, 허용되지 않은 문자
- `Position`: 0, 1, 99, 100과 범위를 벗어난 -1, 101
- `Round`: 1, 2, 99, 100과 범위를 벗어난 0, 101
- `RandomAdvanceDecider`: 전진 기준값을 중심으로 3, 4, 5

`RacingCarsTest`는 참가 순서, 시작 위치, 중복 이름, 전진 판단 적용 순서, 위치 스냅샷과 공동 선두를 검증합니다. `RacingTest`는 시작 규칙, `advanceIfPossible()`의 진행 여부, 설정한 라운드 수만큼의 진행, 100라운드 경계와 종료 후 우승자 조회를 검증합니다.

`RandomAdvanceDeciderTest`는 `FixedRandomGenerator`로 난수 값을 고정해 전진 기준을 검증합니다. `Racing`과 `RacingCars`는 `TestAdvanceDecider`에 정해진 판단 순서를 전달해 경주 결과를 재현합니다.

`ConsoleReader`와 `ConsoleWriter`는 자동차 경주와 관계없는 일반 문자열로 독립적으로 테스트합니다. `RacingFormView`는 입력값 변환과 입력 안내 출력을 검증하고, `RacingResultView`는 라운드 번호, 자동차별 위치와 단독 또는 공동 우승자 출력 형식을 검증합니다.

`RacingController`와 `Apllication`은 객체를 조합하고 실행을 연결하는 구성 코드이므로 별도의 단위 테스트를 작성하지 않습니다.

## Trade-off

### 라운드 결과를 출력하는 방식

경주는 각 라운드가 끝날 때마다 현재 위치를 출력해야 합니다. 도메인에서 발생한 라운드 결과를 출력 계층까지 전달하는 방식으로 다음 세 가지를 비교했습니다.

| 방식 | 장점 | 단점 |
| --- | --- | --- |
| 모든 라운드의 snapshot을 모은 뒤 경주 종료 후 반환 | 경주 실행과 출력 시점을 분리할 수 있고 Controller가 중간 상태를 조회하지 않아도 됩니다. | 모든 라운드 결과를 보관해야 하며, 라운드 직후 출력한다는 실행 흐름과 거리가 생깁니다. 경주가 현재 상태뿐 아니라 과거 결과 이력까지 보유해야 합니다. |
| callback handler를 경주에 전달해 라운드마다 출력 | 라운드가 끝나는 즉시 결과를 전달할 수 있고 별도의 snapshot 누적이 필요하지 않습니다. | Domain이 외부 callback과 협력하게 되어 실행 부수 효과가 도메인 행동 안으로 들어옵니다. 출력 방식과 도메인 실행 사이의 결합도도 높아집니다. |
| 매 라운드 진행 후 `Racing`에 현재 결과를 질의 | `Racing`은 경주 상태와 행동에 집중하고 Controller가 출력 orchestration을 담당할 수 있습니다. 과거 라운드 결과를 저장할 필요도 없습니다. | Controller가 라운드 번호와 위치 같은 조회 API를 사용해야 하므로 결과 표현에 대한 의존이 남습니다. |

현재는 **매 라운드 진행 후 `Racing`에 결과를 질의하는 방식**을 선택합니다. 위치 조회는 도메인 판단을 외부에서 대신 수행하는 것이 아니라 이미 수행된 행동의 결과를 읽는 조회로 봅니다. 따라서 경주 진행 여부와 같은 의사 결정은 `Racing` 내부에 두고, 출력에 필요한 현재 위치는 Controller가 `Racing`에 질의한 뒤 View로 전달합니다.

### 다음 라운드 진행을 결정하는 방식

경주를 반복 실행할 때 다음 라운드를 진행할 수 있는지 판단하는 책임을 어디에 둘지도 비교했습니다.

| 방식 | 장점 | 단점 |
| --- | --- | --- |
| Controller가 진행 가능 여부를 먼저 질의한 뒤 `advance()` 호출 | 진행 가능 여부와 실제 진행 행동이 명시적으로 분리됩니다. | Controller가 경주의 종료 조건이나 상태를 해석해 다음 행동을 결정하게 됩니다. 판단과 행동이 분리되어 `Racing`의 생명주기 규칙이 외부 orchestration에 일부 노출됩니다. |
| `Racing.advanceIfPossible()`이 가능 여부를 판단하고 직접 진행 | 진행 조건과 상태 전이가 `Racing` 안에 응집됩니다. Controller는 `RacingStatus`나 종료 조건을 알 필요가 없습니다. | 진행할 수 없는 호출을 예외가 아닌 정상적인 제어 흐름으로 표현하므로 기존 `advance()`의 강한 상태 계약보다 실패 의미가 약해집니다. |

현재는 **`advanceIfPossible()`이 `RACING` 상태와 남은 라운드 여부를 함께 확인하고, 실제로 한 라운드를 진행했는지를 `boolean`으로 반환하는 방식**을 선택합니다.

Controller의 반복 조건은 다음 형태를 지향합니다.

```java
while (racing.advanceIfPossible()) {
    resultView.printRound(
            racing.completedRounds(),
            raceCount,
            positionsOf(racing)
    );
}
```

이 구조에서 Controller는 `RacingStatus.RACING` 같은 내부 상태 표현을 해석하지 않습니다. `Racing`이 다음 라운드를 진행할 수 있는지 판단하고 상태를 변경하며, Controller는 실제로 라운드가 진행된 경우에만 현재 결과를 조회해 출력합니다.

`positions()`와 같은 조회 API는 유지합니다. Tell, Don't Ask를 모든 조회 제거로 해석하지 않고, **도메인 판단과 상태 전이는 객체 내부에 두되 이미 결정된 결과 데이터는 외부 계층이 조회할 수 있다**는 경계로 적용합니다.

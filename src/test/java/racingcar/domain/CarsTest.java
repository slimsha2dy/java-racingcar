package racingcar.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static racingcar.exception.ExceptionMessage.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CarsTest {
    @Test
    @DisplayName("[Success] 자동차가 정상적으로 생성됨")
    void createCars() {
        List<Car> cars = List.of(new Car("a"), new Car("b"));

        assertThatCode(() -> new Cars(cars))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("[Exception] 중복된 자동차 이름이 있으면 예외를 던진다")
    void createCarsByDuplicateCarNames() {
        List<Car> cars = List.of(new Car("123"), new Car("123"));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Cars(cars))
                .withMessage(NOT_DUPLICATED_CAR_NAME.getMessage());
    }

    @ParameterizedTest
    @MethodSource("InputCarNames")
    @DisplayName("[Exception] 자동차 대수가 2대 미만이거나 10대 초과하면 예외를 던진다")
    void createCarsByInvalidSize(List<String> carNames) {
        List<Car> cars = carNames.stream()
                .map(Car::new)
                .toList();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Cars(cars))
                .withMessage(INVALID_CARS_SIZE.getMessage());
    }

    private static Stream<Arguments> InputCarNames() {
        return Stream.of(
                Arguments.arguments(List.of("1")),
                Arguments.arguments(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"))
        );
    }

    @Test
    @DisplayName("[Success] 자동차 전진 횟수가 가장 많은 차들의 이름을 반환한다")
    void getWinners() {
        Car car1 = new Car("car1");
        car1.move(4);
        car1.move(4);
        Car car2 = new Car("car2");
        car2.move(4);
        car2.move(4);
        Car car3 = new Car("car3");
        car3.move(1);
        car3.move(1);

        Cars cars = new Cars(List.of(car1, car2, car3));

        Assertions.assertThat(cars.getWinners())
                .isEqualTo(List.of("car1", "car2"));
    }

    @Test
    @DisplayName("[Success] Car에서 받은 name과 position들을 Map으로 반환한다")
    void getResults() {
        Cars cars = new Cars(List.of(
                new Car("car1"),
                new Car("car2"),
                new Car("car3")
        ));

        Assertions.assertThat(cars.result())
                .isEqualTo(Map.of("car1", 0, "car2", 0, "car3", 0));
    }
}

from typing import List, Union

SpecType = Union[int, str]


def get_equations(filename: str):
    with open(filename) as i:
        return [
            line.strip() for line in i
        ]


def prepare(equation: str) -> List[SpecType]:
    parts = []
    for c in equation:
        if c == ' ':
            pass
        elif c in ['(', ')', '*', '+']:
            parts.append(c)
        else:
            num = int(c)
            parts.append(num)
    return parts


class Equation:

    def __init__(self, spec: List[SpecType]):
        self.spec = spec
        self.operands: List[str] = []
        self.values: List[Union[int, 'Equation']] = []
        self.result = None
        self.explore()

    def calculate(self, operands=None, values=None):
        if operands is None:
            operands = self.operands
        if values is None:
            values = self.values
        for value in values:
            if isinstance(value, Equation) and value.result is None:
                value.calculate()
        result = get_value(values[0])
        for operand, value in zip(operands, values[1:]):
            value = get_value(value)
            if operand == '*':
                result *= value
            elif operand == '+':
                result += value
            else:
                raise RuntimeError(f'Unknown operand: {operand}')
        self.result = result
        return result

    def explore(self):
        idx = 0
        while idx < len(self.spec):
            item = self.spec[idx]
            if isinstance(item, int):
                self.values.append(item)
                idx += 1
            elif item == '(':
                close_paren = self.find_closing_paren(idx+1)
                self.values.append(Equation(self.spec[idx+1:close_paren]))
                idx = close_paren + 1
            elif isinstance(item, str):
                self.operands.append(item)
                idx += 1
            else:
                raise RuntimeError(f'Unknown operation: {item}')

    def find_closing_paren(self, idx: int) -> int:
        open_count = 1
        while open_count > 0:
            if self.spec[idx] == '(':
                open_count += 1
            if self.spec[idx] == ')':
                open_count -= 1
            idx += 1
        return idx-1

    def __str__(self):
        return f'{self.operands} + {[str(v) for v in self.values]}'


class EquationSumPrecedence(Equation):

    def __init__(self, spec: List[str]):
        super().__init__(spec)

    def calculate(self, operands=None, values=None):
        if operands is None:
            operands = self.operands
        if values is None:
            values = self.values
        for value in values:
            if isinstance(value, Equation) and value.result is None:
                value.calculate()
        new_operands = []
        new_values = [get_value(values[0])]
        for operand, value in zip(operands, values[1:]):
            if operand == '*':
                new_operands.append('*')
                new_values.append(get_value(value))
            elif operand == '+':
                p_value = get_value(value)
                new_values[-1] = p_value + new_values[-1]
        self.result = super().calculate(new_operands, new_values)
        return self.result

    def explore(self):
        idx = 0
        while idx < len(self.spec):
            item = self.spec[idx]
            if isinstance(item, int):
                self.values.append(item)
                idx += 1
            elif item == '(':
                close_paren = self.find_closing_paren(idx+1)
                self.values.append(EquationSumPrecedence(self.spec[idx+1:close_paren]))
                idx = close_paren + 1
            elif isinstance(item, str):
                self.operands.append(item)
                idx += 1
            else:
                raise RuntimeError(f'Unknown operation: {item}')


def get_value(value: Union[int, Equation, EquationSumPrecedence]) -> int:
    if isinstance(value, (Equation, EquationSumPrecedence)):
        return value.result
    return value


def prepare_eqs(eqs: List[str]) -> List[List[str]]:
    processed = []
    for eq in eqs:
        processed.append(prepare(eq))
    return processed


if __name__ == '__main__':
    equations = get_equations('day_18_input.txt')
    t_equations = prepare_eqs(equations)
    eq_sum = 0
    eq_sum_precedence = 0
    for teq in t_equations:
        eq = Equation(teq)
        eqs = EquationSumPrecedence(teq)
        print(eq)
        print('Normal', eq.calculate())
        print('Sum precedence', eqs.calculate())
        eq_sum += eq.calculate()
        eq_sum_precedence += eqs.calculate()
    print(f'Total sum is {eq_sum}')
    print(f'Total with sum precedence is {eq_sum_precedence}')

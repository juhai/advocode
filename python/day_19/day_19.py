from typing import List, Dict


def read_rules(filename: str) -> List[str]:
    with open(filename) as i:
        return [line.strip() for line in i if ':' in line]


def read_input(filename: str) -> List[str]:
    with open(filename) as i:
        return [line.strip() for line in i if line[0] in ['a', 'b']]


def build_rules(rules: List[str]):
    bootstrap_rules = {}
    for rule in rules:
        rule_id = int(rule.split(':')[0])
        if '"' in rule:  # char rule
            bootstrap_rules[rule_id] = [rule.strip().split('"')[1]]
            continue
        values = rule[rule.index(':')+1:].strip().split('|')
        current_rules = []
        for value in values:
            current_rules.append([int(v) for v in value.strip().split()])
        bootstrap_rules[rule_id] = current_rules
    return bootstrap_rules


def match(rules: Dict[int, List[List[int]]], word: str):
    queue = [(rules[0][0], word)]
    while queue:
        rule, word = queue.pop(0)
        if not rule and not word:
            return True
        if (not rule and word) or (rule and not word):
            continue
        first_referred_rule = rules[rule[0]]
        if len(first_referred_rule) == 1 and isinstance(first_referred_rule[0], str):
            if word[0] != first_referred_rule[0]:
                continue
            queue.append((rule[1:], word[1:]))
            continue
        for alternative in rules[rule[0]]:
            queue.append((alternative + rule[1:], word))
    return False


if __name__ == '__main__':
    filename = 'day_19_input.txt'
    rule_data = read_rules(filename)
    inputs = read_input(filename)
    rules = build_rules(rule_data)
    rules[8] = [[42], [42, 8]]
    rules[11] = [[42, 31], [42, 11, 31]]
    matches = 0
    for i in inputs:
        is_match = match(rules, i)
        if is_match:
            matches += 1
        print(f'{i} is_match? {is_match}')
    print(f'Total matches are {matches} out of {len(inputs)} inputs')

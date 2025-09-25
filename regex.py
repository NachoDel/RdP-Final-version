import re
import os

file_path = "petrinet/logs/transitions.txt"

with open(file_path, 'r', encoding='utf-8') as file:
    data = file.read()

pattern = r'(T0)(.*?)(T1)(.*?)((T3)(.*?)(T4)(.*?)((T7)(.*?)(T8)(.*?)|(T6)(.*?)(T9)(.*?)(T10)(.*?))|(T2)(.*?)(T5)(.*?)((T7)(.*?)(T8)(.*?)|(T6)(.*?)(T9)(.*?)(T10)(.*?)))(T11)'
replacement = (r'\g<2>\g<4>\g<7>\g<9>\g<12>\g<14>\g<16>\g<18>\g<20>\g<22>\g<24>\g<27>\g<29>\g<31>\g<33>\g<35>')

invariant_patterns = [
    (r'T0.*?T1.*?T3.*?T4.*?T7.*?T8.*?T11', 'T0-T1-T3-T4-T7-T8-T11'),
    (r'T0.*?T1.*?T3.*?T4.*?T6.*?T9.*?T10.*?T11', 'T0-T1-T3-T4-T6-T9-T10-T11'),
    (r'T0.*?T1.*?T2.*?T5.*?T7.*?T8.*?T11', 'T0-T1-T2-T5-T7-T8-T11'),
    (r'T0.*?T1.*?T2.*?T5.*?T6.*?T9.*?T10.*?T11', 'T0-T1-T2-T5-T6-T9-T10-T11')
]
invariant_counts = [0] * len(invariant_patterns)

total_count = 0

while True:
    match = re.search(pattern, data)
    if not match:
        break

    extracted_match = match[0]
    # print("Patrón extraído:", extracted_match)

    for i, (invariant_pattern, name) in enumerate(invariant_patterns):
        if re.fullmatch(invariant_pattern, extracted_match):
            invariant_counts[i] += 1
        #    print(f"  - Coincide con: {name}")
            break

    data = re.sub(pattern, replacement, data, count=1)
    total_count += 1



print("Data sobrante: " + data)
print("\n" + str(total_count) + " invariantes encontradas.")
for i, (count, (_, pattern)) in enumerate(zip(invariant_counts, invariant_patterns)):
    print(f"Invariante de transición {pattern}: ha sucedido {count} veces")

if data.strip():
    print("El test falló, sobraron transiciones")
else:
    print("El test finalizó ok")
import random
from pathlib import Path

def gerar_cpf():
    while True:
        cpf = [random.randint(0, 9) for i in range(9)]
        if cpf != cpf[::-1]:
            break

    for i in range(9, 11):
        value = sum((cpf[num] * ((i + 1) - num) for num in range(0, i)))
        digit = ((value * 10) % 11) % 10
        cpf.append(digit)

    result = ''.join(map(str, cpf))
    return result

cpfs = []

for i in range(10):
    cpfs.append(gerar_cpf())


for cpf in cpfs:
    caminho_pasta_compras = Path("cpfs/"+ cpf + "/compras")
    caminho_pasta_vendas = Path("cpfs/"+ cpf + "/vendas")

    caminho_pasta_compras.mkdir(exist_ok=True, parents=True)
    caminho_pasta_vendas.mkdir(exist_ok=True, parents=True)


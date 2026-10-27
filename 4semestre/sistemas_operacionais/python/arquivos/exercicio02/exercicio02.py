import random
import csv
import sys
import os

caminho_pasta_arquivos = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
sys.path.append(caminho_pasta_arquivos)

from exercicio01.exercicio01 import gerar_cpf

with open('nomes_masculino.txt', 'r', encoding='utf-8') as arq_masc:
    nomes_masc = [linha.strip() for linha in arq_masc]

with open('nomes_feminino.txt', 'r', encoding='utf-8') as arq_fem:
    nomes_fem = [linha.strip() for linha in arq_fem]

with open('sobrenomes.txt', 'r', encoding='utf-8') as arq_sobrenomes:
    sobrenomes = [linha.strip() for linha in arq_sobrenomes]

todos_os_nomes = nomes_masc + nomes_fem

opcoes_sexo = ['masculino', 'feminino', 'não binário', 'não declarado']
cidades = ['Ubatuba', 'Caraguatatuba', 'São Sebastião', 'Ilhabela']

quantidade_de_pessoas = 50 

with open('pessoas.csv', 'w', encoding='utf-8', newline='') as arquivo_csv:
    escritor = csv.writer(arquivo_csv)
    
    escritor.writerow(['cpf', 'nome', 'sexo', 'cidade', 'estado'])
    
    for i in range(quantidade_de_pessoas):
        sexo = random.choice(opcoes_sexo)
        
        if sexo == 'masculino':
            nome = random.choice(nomes_masc)
        elif sexo == 'feminino':
            nome = random.choice(nomes_fem)
        else:
            nome = random.choice(todos_os_nomes)
            
        sobrenome = random.choice(sobrenomes)
        nome_completo = f"{nome} {sobrenome}"
        
        cidade = random.choice(cidades)
        cpf = gerar_cpf()
        estado = 'SP'
        
        escritor.writerow([cpf, nome_completo, sexo, cidade, estado])

print("Arquivo 'dados_pessoas.csv' gerado com sucesso!")
from E3fibonacci import fibonacci
from E5euler import euler
from fatorial import fatorial

class NegativeValue(Exception):
    """Valor menor ou igual a zero"""

class Matematica:
    def calcularFibonnaci(self):
        try:
            limite = int(input("Digite até que número deseja gerar a sequência de fibonacci: "))
            if limite <= 0:
                raise NegativeValue("Insira um número maior que zero.")
            
            return fibonacci(limite)
        except OverflowError:
            print("Resultado muito longo para ser representado.")

    def calcularEuler(self):
        try:
            quantidade = int(input("Digite quantos números deseja gerar na sequência de euler: "))
            if quantidade <= 0:
                raise NegativeValue("Insira um número maior que zero.")

            return euler(quantidade)
        except OverflowError:
            print("Resultado muito longo para ser representado.")
    
    def calcularFatorial(self):
        try:
            numero = int(input("Digite um número para calcular o fatorial:"))
            if numero <= 0:
                raise NegativeValue("Insira um número maior que zero.")

            return fatorial(numero)
        except OverflowError:
            print("Resultado muito longo para ser representado.")
    
math = Matematica()

try:
    sequenciaFibonacci = math.calcularFibonnaci()
    numerosEuler = math.calcularEuler()
    fatorado = math.calcularFatorial()

    print("Fibonnaci: ")
    for numero in sequenciaFibonacci:
        print(numero, end=" ")

    print("\n\nEuler:")
    for numero in numerosEuler:
        print(numero, end=" ")
        
    print(f"\n\nFatorial: {fatorado}")
except NegativeValue as error:
    print(error)
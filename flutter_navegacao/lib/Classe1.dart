
class Classe1{
  String nome;
  DateTime data;

  Classe1(this.nome, this.data);
  String get getNome => nome;
  String getTudo(){
    return nome + data.toIso8601String();
  }

}
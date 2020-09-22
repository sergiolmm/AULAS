
class usuario{
  String nome;
  String email;
  DateTime data;

  usuario(this.nome,this.data,{this.email="sergio@unicamp.br"});

  String get getNome => nome;
  String get getEmail => email;
  String getTudo(){
    return nome + data.toIso8601String();
  }


}
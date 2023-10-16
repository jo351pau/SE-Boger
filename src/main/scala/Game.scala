class Game(val fields: List[Field]){

    def this(numberOfFields: Int, numberOfFigures: Int) = this(List(Field(numberOfFigures)) ++ List.fill(numberOfFields-1)(Field()))

    override def toString: String = fields.mkString("\t")
}
package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;

public class NullStatementList extends StatementList {
  public NullStatementList(Location l) {
      super(l);
  }
  public void accept(Visitor v) {
  }
}


package Parse;

import java.util.ArrayList;
import Absyn.*;
import java.util.Optional;
import Parse.antlr_build.Parse.*;
import org.antlr.v4.runtime.ParserRuleContext;


/*
 * Hello, I assume that you have read the material in gParser.g4
 *
 * This file is your "Visitor". 
 *
 * Your job is to write visit functions for each parse rule in the gParser.g4 
 * file. Each visit function needs to return the corresponding Absyn node.
 *
 * The driver file you have been provided will print whatever is returned from
 * this visitor. If you successfully return the Absyn nodes, you will see them 
 * print in the terminal.
 *
 * If you get stuck of lost: Each context object can be found
 * in gParser.java. Just search "Context".
 *
 * 
*/

public class ASTBuilder extends gParserBaseVisitor<Absyn> {

   @Override
   public Absyn visitProgram(gParser.ProgramContext ctx) {
      DeclList decls = new DeclList(0);
      for (gParser.DeclarationContext dctx : ctx.declaration()) {
        decls.list.add((Decl)visit(dctx));
      }
      return decls;
   }
   
// ===== Part 3: Statements (Control Flow Layer) =====

@Override
public Absyn visitCompStmt(gParser.CompStmtContext ctx) {
    DeclList decls = new DeclList(0);
    for (gParser.DeclarationContext dctx : ctx.declaration()) {
        decls.list.add((Decl) visit(dctx));
    }

    StmtList stmts = new StmtList(0);
    for (gParser.StatementContext sctx : ctx.statement()) {
        stmts.list.add((Stmt) visit(sctx));
    }

    return new CompStmt(0, decls, stmts);
}

@Override
public Absyn visitIfStmt(gParser.IfStmtContext ctx) {
    Exp condition = (Exp) visit(ctx.expr());
    Stmt thenStmt = (Stmt) visit(ctx.statement());
    return new IfStmt(0, condition, thenStmt, new EmptyStmt(0));
}

@Override
public Absyn visitIfElseStmt(gParser.IfElseStmtContext ctx) {
    Exp condition = (Exp) visit(ctx.expr());
    Stmt thenStmt = (Stmt) visit(ctx.statement(0));
    Stmt elseStmt = (Stmt) visit(ctx.statement(1));
    return new IfStmt(0, condition, thenStmt, elseStmt);
}

@Override
public Absyn visitWhileStmt(gParser.WhileStmtContext ctx) {
    Exp condition = (Exp) visit(ctx.expr());
    Stmt body = (Stmt) visit(ctx.statement());
    return new WhileStmt(0, condition, body);
}

@Override
public Absyn visitExprStmt(gParser.ExprStmtContext ctx) {
    Exp expression = (Exp) visit(ctx.expr());
    return new ExprStmt(0, expression);
}

@Override
public Absyn visitReturnStmt(gParser.ReturnStmtContext ctx) {
    Exp returnValue = (Exp) visit(ctx.initializer());
    return new ReturnStmt(0, returnValue);
}

@Override
public Absyn visitBreakStmt(gParser.BreakStmtContext ctx) {
    return new BreakStmt(0);
    }

   @Override
   public Absyn visitParenExp(gParser.ParenExpContext ctx){
      // Just visit the inner expression and return it
      return visit(ctx.expr());
   }
   @Override
   public Absyn visitBinOp(gParser.BinOpContext ctx){
      // Get left expression and visit it
      Exp left = (Exp)visit(ctx.expr(0));
      // Get right expression and visit it
      Exp right = (Exp)visit(ctx.expr(1));
      // Get operator as a string
      String operator = ctx.op.getText();
      // Create and return a BinOp node
      return new BinOp(0, left, operator, right);
   }
   @Override
   public Absyn visitUnaryExp(gParser.UnaryExpContext ctx){
      // Get the operator from the unary_operator rule
      String prefix = ctx.unary_operator().getText();
      // Get and visit the expression being operated on
      Exp exp = (Exp)visit(ctx.expr());
      // Create and return UnaryExp node
      return new UnaryExp(0, prefix, exp);
   }
   @Override
   public Absyn visitFunExp(gParser.FunExpContext ctx){
      // Get the function name
      Exp name = (Exp)visit(ctx.expr(0));
      // Create list to hold parameters
      ExpList params = new ExpList(0);
      // Loop through all parameter expressions
      for (int i = 1; i < ctx.expr().size(); i++){
         params.list.add((Exp)visit(ctx.expr(i)));
      }
      // Create and return FunExp node
      return new FunExp(0, name, params);
   }
   @Override
   public Absyn visitArrayExp(gParser.ArrayExpContext ctx){
      // Get the array name
      Exp name = (Exp)visit(ctx.expr(0));
      // Create list to hold all indices
      ExpList indices = new ExpList(0);
      // Loop through all index expressions
      for(int i = 1; i < ctx.expr().size(); i++){
         indices.list.add((Exp)visit(ctx.expr(i)));
      }
      // Create and return ArrayExp node
      return new ArrayExp(0, name, indices);
   }
   @Override
   public Absyn visitAssignExp(gParser.AssignExpContext ctx){
      // Get the left-hand side
      Exp left = (Exp)visit(ctx.expr());
      // Get the right-hand side
      Exp right = (Exp)visit(ctx.initializer());
      // Create and return AssignExp node
      return new AssignExp(0, left, right);
   }
   @Override //DecLit accounts for hex, oct. and decimal now
    public Absyn visitDecLit(gParser.DecLitContext ctx) {
        String text = ctx.DECIMAL_LITERAL().getText();
        int value;
        if (text.startsWith("0X") || text.startsWith("0x")) {
            value = Integer.parseInt(text.substring(2), 16);
        } else if (text.startsWith("0") && text.length() > 1) {
            value = Integer.parseInt(text.substring(1), 8);
        } else {
            value = Integer.parseInt(text);
        }
        return new DecLit(0, value);
    }
   @Override
   public Absyn visitID(gParser.IDContext ctx){
      // Get the identifier text
      String name = ctx.ID().getText();
      // Create and return ID node
      return new ID(0, name);
   }
   @Override
   public Absyn visitStrLit(gParser.StrLitContext ctx){
      // Get the string literal text
      String value = ctx.STRING_LITERAL().getText();
      // Create and return StrLit node
      return new StrLit(0, value);
   }


}


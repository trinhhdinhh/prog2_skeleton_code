
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

@Override
public Absyn visitType(gParser.TypeContext ctx) {

    boolean isConst = ctx.CONST() != null;
    String name = ctx.type_name().getText();
    int ptrs = ctx.STAR().size();

    DeclList brackets;
    if (ctx.brackets_list() != null) {
        brackets = (DeclList) visit(ctx.brackets_list());
    } else {
        brackets = new DeclList(0);
    }

    return new Type(0, isConst, name, ptrs, brackets);
}

//Part 2

   @Override
   public Absyn visitVarDecl(gParser.VarDeclContext ctx){
      Type type = (Type) visit(ctx.type());
      String name = ctx.ID().getText();

      Exp init;

      if (ctx.initialization() == null) {
           init = new EmptyExp(0);
      } else {
           init = (Exp) visit(ctx.initialization().initializer());
      }

      return new VarDecl(0, type, name, init);
   }

    @Override
    public Absyn visitFunDecl(gParser.FunDeclContext ctx) {

        Type retType = (Type) visit(ctx.type());
        String name = ctx.ID().getText();

        DeclList params;
        if (ctx.parameters() != null) {
            params = (DeclList) visit(ctx.parameters());
        } else {
            params = new DeclList(0);
        }

        Stmt body = null;
        if (ctx.statement() != null) {
            body = (Stmt) visit(ctx.statement());
        }
        return new FunDecl(0, retType, name, params, body);
    }

    @Override
    public Absyn visitTypedefDecl(gParser.TypedefDeclContext ctx) {
        Type type = (Type) visit(ctx.type());
        String name = ctx.ID().getText();
        return new Typedef(0, type, name);
    }

    @Override
    public Absyn visitStructOrUnionDecl(gParser.StructOrUnionDeclContext ctx) {
        String name = ctx.ID(0).getText();

        DeclList members = new DeclList(0);
        for (int i = 0; i < ctx.type().size(); i++) {
            Type t = (Type) visit(ctx.type(i));
            String field = ctx.ID(i + 1).getText();
            members.list.add(new StructMember(0, t, field));
        }

        if (ctx.STRUCT() != null) {
            return new StructDecl(0, name, members);
        } else {
            return new UnionDecl(0, name, members);
        }
    }

    @Override
    public Absyn visitParameters(gParser.ParametersContext ctx) {
        DeclList params = new DeclList(0);
        for (int i = 0; i < ctx.type().size(); i++) {
            Type t = (Type) visit(ctx.type(i));
            String name = ctx.ID(i).getText();
            params.list.add(new Parameter(0, t, name));
        }
        return params;
    }
}


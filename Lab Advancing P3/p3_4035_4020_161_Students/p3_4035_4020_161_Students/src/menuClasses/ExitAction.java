package menuClasses;

import systemClasses.SystemController;

public class ExitAction implements Action {

	@Override
	public void execute(Object arg) {
		SystemController sc = (SystemController) arg; 
		sc.getMenuStack().pop(); 
	}

}

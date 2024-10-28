package command;

import model.pa.model.AirportNetwork;

/**
 * Class CopyCommand extended class of Command
 */
public class CopyCommand extends Command {

    public CopyCommand(AirportNetwork model) {
        super(model);
    }

    /**
     * Executes the backup to create a new instance of the object(model)
     * @return boolean
     */
    @Override
    public boolean execute() {
        backup();
        return true;
    }
}
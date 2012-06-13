package de.proskor.ea
import cli.EA.IRepository
import de.proskor.ea.model.EARepository
import de.proskor.Extension
import de.proskor.model.Repository
import cli._

trait EAAdapter {
  this: Extension =>

  val MainMenu = "-CFT Integration"
  val MergeMenuItem = "Merge..."
  val DiagramMenuItem = "Create Diagram"
  val FailureModesMenuItem = "Show Failure Modes"
  val PrintIdMenuItem = "Print Element Id"
  val TestMenuItem = "Test"
    
  val EventsMenuItem = "-Events";
  val NewEventTypeMenuItem = "New Event Type / Show List";
  val NewEventInstanceMenuItem = "New Event Instance";

  val eaEventAdapter = new EAEventMenuAdapter();
  
  def EA_OnPostInitialized(repository: IRepository) {
    Repository.current = Some(new EARepository(repository))
    start()
  }

  
  def EA_Disconnect {
    stop()
    EADataBase.saveFailureModes()
  }

  
  def EA_GetMenuItems(repository: IRepository, location: String, menuName: String): Any = menuName match {
    case "" => MainMenu
    case EventsMenuItem => Array(NewEventTypeMenuItem, NewEventInstanceMenuItem)
    //case NewEventInstanceMenuItem => eaEventAdapter.EA_GetMenuItems_ForEventInstanceMenuItem(IRepository)
    case MainMenu => Array(EventsMenuItem, MergeMenuItem, DiagramMenuItem, FailureModesMenuItem, PrintIdMenuItem, TestMenuItem)
  }
  
  
  def EA_OnContextItemChanged(repository: IRepository, guid: String, objectType : cli.EA.ObjectType) {
  }
  
  
  /*  "isEnabled & isChecked" verwenden Call-By-Reference um die Werte zu verändern. Da dies in Java/Scala jedoch nicht m�glich ist,
   *  verteile ich beide Variablen auf zwei unterschiedliche Methoden, die die Werte als Return-Value liefern.
   *  
   *  Hier die original-Methode in C#: 
   *  public void EA_GetMenuState(EA.Repository Repository, string Location, string MenuName, string ItemName, ref bool IsEnabled, ref bool IsChecked)
  */
  
  def EA_GetMenuState_IsEnabled(repository: IRepository, menuLocation: String, menuName: String, itemName: String) : Boolean = menuName match {
    case NewEventTypeMenuItem => eaEventAdapter.newEventTypeMenu_isEnabled(repository)
    case NewEventInstanceMenuItem => eaEventAdapter.newEventInstanceMenu_isEnabled(repository)
    case _ => true
  }  
  
  def EA_GetMenuState_IsChecked(repository: IRepository, menuLocation: String, menuName: String, itemName: String) : Boolean = {
    return false;
  }

  def EA_MenuClick(repository: IRepository, menuName: String, itemName: String) = itemName match {
    case NewEventTypeMenuItem => eaEventAdapter.createEventType(repository)
    case NewEventInstanceMenuItem => eaEventAdapter.createEventInstance(repository)

    case MergeMenuItem => merge()
    case DiagramMenuItem => createDiagram()
    case FailureModesMenuItem => failureModes()
    case PrintIdMenuItem => printId()
    case TestMenuItem => test()
    case _ =>
  }
}
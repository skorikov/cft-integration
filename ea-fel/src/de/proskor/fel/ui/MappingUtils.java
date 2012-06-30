package de.proskor.fel.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/*
 * ============================================================================================================================================
 * Mapping Problem: 
 * ============================================================================================================================================
 * Einseitiges überschreiben eines Mappings. Gibt es beispielsweise ein Mapping A<->B,
 * so existiert dieses Mapping in ZWEI HashMaps. Das heißt beide Objekte sind einmal Schlüssel
 * und einmal Wert. Wird jetzt nur ein Objekt als Schlüssel überschrieben, so kommt ein
 * inkonsistenter Zustand der Hash-Map zustande.
 *
 * Inkonsistenz: 
 * Das Mapping eines Mappings muss das Element selbst liefern!
 * D.h. x == mapping(mapping(x))
 * Diese Eigenschaft wird durch das beschriebene Problem jedoch zerstört!
 * 
 * Lösung:
 * Vor dem Schreiben eines Mappings werden alle alten Mappings beider Objekte
 * gelöscht.
 * 
 * --------------------------------------------------------------------------------------------------------------------------------------------
 * Das heißt:
 * --------------------------------------------------------------------------------------------------------------------------------------------
 * put(A, B)
 * mapping(A) == B.
 * mapping(B) == A.
 * ==> 	A == mapping(mapping(A))
 * ==> 	B == mapping(mapping(B))
 * 
 * put(A, C)
 * mapping(A) == C.
 * mapping(B) == A.
 * mapping(C) == A.
 * ==> 	B != mapping(mapping(B))		
 * 		da: B == mapping(mapping(B)) == mapping(A) == C  ist Widerspruch!
 * 
 * 
 * --------------------------------------------------------------------------------------------------------------------------------------------
 * Interner HashMap-Zustand:
 * --------------------------------------------------------------------------------------------------------------------------------------------
 * put(A, B)
 * xy-Hash: 	[A -> B]
 * yx-Hash: 	[B -> A]
 * 
 * put(A, C)
 * xy-Hash: 	[A -> C] 	<überschreibt Mapping [A -> B] !>
 * yx-Hash: 	[B -> A]
 * 				[C -> A]
 *
 * 
 * --------------------------------------------------------------------------------------------------------------------------------------------
 * Mappings sicher löschen:
 * --------------------------------------------------------------------------------------------------------------------------------------------
 * Vor dem Einfügen eines neuen Mappings durch put(A, C):
 * if (mapping(A) != null)
 * 		yx-Hash.remove(mapping(A))
 * if (mapping(C) != null)
 * 		xy-Hash.remove(mapping(C))
 * 
 * ============================================================================================================================================
 * ============================================================================================================================================
 */

/**
 * @author Tewanima Löwe, 2010-11-18
 */
public class MappingUtils {
	/**
	 * Erstellt eine <b>Bijektion</b> zwischen jeweils zwei Objekten der Klassen <b>ObjectA</b> und <b>ObjectB</b>, 
	 * sodass <b>objA</b> auf <b>objB</b> <i>gemappt</i> wird und umgekehrt.
	 * <p>
	 * <b>Es gilt für alle Mappings (objA, objB):</b><br>
	 * <i>objA == mapping(objB)</i>  <b>sowie</b>  <i>objB == mapping(ObjA)</i>. <br>
	 * <i>objA == mapping(mapping(objA))</i>  <b>und</b>  <i>objB == mapping(mapping(objB))</i>. 
	 */
	public static class ObjectMapping<ObjectA, ObjectB>  {
		private HashMap<ObjectA, ObjectB> objectAToObjectBMappingHash;
		private HashMap<ObjectB, ObjectA> objectBToObjectAMappingHash;

		public ObjectMapping() {
			clear();
		}
		
		/**
		 * Diese Methode verhindert <b>Inkonsistenzen</b> innerhalb der beiden HashMaps.
		 * Dazu siehe das oben kommentierte Mapping-Problem.
		 */
		private void assureConsistency(ObjectA objA, ObjectB objB) {
			removeObjectAMapping(objA);
			removeObjectBMapping(objB);
		}
		
		public void put(ObjectA objA, ObjectB objB) {
			assureConsistency(objA, objB);
			
			objectAToObjectBMappingHash.put(objA, objB);
			objectBToObjectAMappingHash.put(objB, objA);
		}
		
		public void removeObjectAMapping(ObjectA objA) {
			/*
			 * Sicheres Löschen der Bijektion:
			 * 1. B = mapping(A) holen und B als Schlüssel löschen.
			 * 2. A als Schlüssel löschen.
			 */
			if (objectAToObjectBMappingHash.containsKey(objA)) {
				ObjectB mappedObjB = objectAToObjectBMappingHash.get(objA); // Mapping(ObjA) [== B] aus AB-Hash holen
				
				objectBToObjectAMappingHash.remove(mappedObjB); // B als Schlüssel verwenden um aus BA-Hash zu löschen. 
				objectAToObjectBMappingHash.remove(objA); // A selbst löschen 
			}
		}

		public void removeObjectBMapping(ObjectB objB) {
			if (objectBToObjectAMappingHash.containsKey(objB)) {
				ObjectA mappedObjA = objectBToObjectAMappingHash.get(objB);
				
				objectAToObjectBMappingHash.remove(mappedObjA);
				objectBToObjectAMappingHash.remove(objB);
			}			
		}
		
		public boolean containsObjectAMapping(ObjectA objA) {
			return objectAToObjectBMappingHash.containsKey(objA);
		}

		public boolean containsObjectBMapping(ObjectB objB) {
			return objectBToObjectAMappingHash.containsKey(objB);
		}
		
		public ObjectB getObjectAMapping(ObjectA objA) {
			return objectAToObjectBMappingHash.get(objA);
		}

		public ObjectA getObjectBMapping(ObjectB objB) {
			return objectBToObjectAMappingHash.get(objB);
		}

		/**
		 * Liefert alle <b>ObjectA</b> Instanzen, für die es ein <i>Mapping</i> gibt.
		 * @see #getMappedObjectBs()
		 */
		public ArrayList<ObjectA> getMappedObjectAs() {
			ArrayList<ObjectA> mappedObjects = new ArrayList<ObjectA>();			
			Iterator<ObjectA> iter = objectAToObjectBMappingHash.keySet().iterator();
			
			while (iter.hasNext()) 
				mappedObjects.add(iter.next());
				
			return mappedObjects;
		}

		/**
		 * Liefert alle <b>ObjectB</b> Instanzen, für die es ein <i>Mapping</i> gibt.
		 * @see #getMappedObjectAs()
		 */
		public ArrayList<ObjectB> getMappedObjectBs() {
			ArrayList<ObjectB> mappedObjects = new ArrayList<ObjectB>();			
			Iterator<ObjectB> iter = objectBToObjectAMappingHash.keySet().iterator();
			
			while (iter.hasNext()) 
				mappedObjects.add(iter.next());
				
			return mappedObjects;
		}

		public void clear() {
			objectAToObjectBMappingHash = new HashMap<ObjectA, ObjectB>();
			objectBToObjectAMappingHash = new HashMap<ObjectB, ObjectA>();
		}
	}
}
#KW
  
KW is a J2EE framework "à la" Struts2. 

I developed it for an academic exam about Web Technologies in 2009, and I decided to release as public software, to show how it can be realized an MVC web framework which use injection.


Of course no ssl nor secure password support is supported. It comes just as an experiment and for education purposes.


All is reflection based, and business (controller) logic level maps view level just using field names reflection.

A complete example is available [here](../..//kw_archimedes_kv_optimusprime__sample)

Starting from jsp form:
<pre>
<code>
&lt;form action=&quot;ConcertLogicInsert&quot; name=&quot;concertsinsertform&quot; id=&quot;concertsinsertform&quot; method=&quot;post&quot;&gt;
	&lt;fieldset&gt;
		&lt;fieldset&gt;
			&lt;label for=&quot;concertCode&quot;&gt;Codice Concerto:&lt;/label&gt;
			&lt;input type=&quot;text&quot; id=&quot;concertCode&quot; name=&quot;concertCode&quot;&gt;
			&lt;span class=&quot;hint&quot;&gt;Il campo deve essere di caratteri numerici./span&gt;
		&lt;/fieldset&gt;
		&lt;fieldset&gt;
			&lt;label for=&quot;groupName&quot;&gt;Nome del Gruppo:&lt;/label&gt;
			&lt;input type=&quot;text&quot; id=&quot;groupName&quot; name=&quot;groupName&quot;&gt;
			&lt;span class=&quot;hint&quot;&gt;Il campo deve essere di caratteri alfabetici.&lt;/span&gt;
		&lt;/fieldset&gt;
		&lt;fieldset&gt;
			&lt;label for=&quot;concertDate&quot;&gt;Data del Concerto:&lt;/label&gt;
			&lt;input type=&quot;text&quot; id=&quot;concertDate&quot; name=&quot;concertDate&quot;&gt;
			&lt;span class=&quot;hint&quot;&gt;Il campo deve essere di caratteri numerici.&lt;/span&gt;
		&lt;/fieldset&gt;
		&lt;fieldset&gt;
			&lt;input type=&quot;submit&quot; value=&quot;submit&quot;&gt;
			&lt;input type=&quot;submit&quot; value=&quot;submit ajax&quot; onclick=&quot;ajaxedInsertingCheckAndSubmit()&quot; style=&quot;width:100px&quot;&gt;
			&lt;input type=&quot;reset&quot; value=&quot;reset&quot;&gt;
			&lt;span class=&quot;null&quot;&gt;&lt;/span&gt;
		&lt;/fieldset&gt;
	&lt;/fieldset&gt;
&lt;/form&gt;
</code>
</pre>

Careful to:

1. the form action "ConcertLogicInsert"  <- this must be exist in kw.xml configuration file (see below) 
2. the input fields with ids "concertCode","groupName","concertDate" <- these must be exist with same name in bean java class (see below Concert.java)

Below, a kw.xml sample configuration file, with various maps - for this example we refer to first "concertLogicInsert", but other nodes are showed for better understanding the mechanisms behind the scenes.
<pre>
<code>
&lt;logics xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;
		xsi:noNamespaceSchemaLocation=&quot;kw.xsd&quot;&gt;
	&lt;logic&gt;
		&lt;name&gt;ConcertLogicInsert&lt;/name&gt;
		&lt;class&gt;festival.logic.ConcertLogicInsert&lt;/class&gt;
		&lt;ok&gt;/pages/insert/inserted.jsp&lt;/ok&gt;
		&lt;input&gt;/pages/insert/inserted.jsp&lt;/input&gt;
	&lt;/logic&gt;
	&lt;logic&gt;
		&lt;name&gt;ConcertLogicSearch&lt;/name&gt;
		&lt;class&gt;festival.logic.ConcertLogicSearch&lt;/class&gt;
		&lt;ok&gt;/pages/search/result.jsp&lt;/ok&gt;
		&lt;input&gt;/pages/search/result.jsp&lt;/input&gt;
	&lt;/logic&gt;
	&lt;logic&gt;
		&lt;name&gt;SomeFullLogic&lt;/name&gt;
		&lt;class&gt;net.iubris.flights.SomeLogic&lt;/class&gt;
		&lt;ok&gt;/ok_2.jsp&lt;/ok&gt;
		&lt;input&gt;/input_2.jps&lt;/input&gt;
		&lt;error&gt;/error_2.jsp&lt;/error&gt;		
	&lt;/logic&gt;	
	&lt;logic&gt;
		&lt;name&gt;SomeInutilLogic&lt;/name&gt;
		&lt;class&gt;net.iubris.flights.SomeInutilLogic&lt;/class&gt;			
	&lt;/logic&gt;	
&lt;/logics&gt;
</code>
</pre>
  

In web.xml KW are configured as Filter (J2EE Filter) intercepting flows, so form data are passed to java class mapping the form ("festival.logic.ConcertLogicInsert" in this example), then the response are redirect to "ok" or "input" or "error" jsp page specified.

And a "bean" class "Concert.java" is as below:

<pre>
<code>
public class Concert {	
	private int concertCode;
	private String groupName;
	private int concertDate;
	// accessors get/set are mandatory, but not reported for simplicity
}
</code>
</pre>



Finally, the ConcertLogicInsert.java:
<pre>
<code>
public class ConcertLogicInsert implements KWActionLogic {
	
	// with this annotation the bean "Concert" are injected from jsp form
	@KW(inject=true)
	private Concert concertToInsert;
	
	// with this annotation the "result" are handled for OK or INPUT flow 
	@KW(target={Execution.OK,Execution.INPUT})
	private ConcertInsertResult concertInsertResult = new ConcertInsertResult();
	
	public void setConcertToInsert(Concert concertToInsert) {
		this.concertToInsert = concertToInsert;
	}
	
	public ConcertInsertResult getConcertInsertResult() {
		return concertInsertResult;
	}


	// inside this method there are business logic
	@Override
	public Execution execute() {
		
		// concert are stored in result class for next step
		this.concertInsertResult.setConcert(concertToInsert);
		
		// a transactional service is istanced
		FestivalService bs = new FestivalService();
		// if this concert is not existant in db, it's stored and "OK" result is showed
		if (bs.insert(concertToInsert)) {
			this.concertInsertResult.setConcertExistant(false);						
			return Execution.OK;
		}

		// if concert is existant, we are here, and a "existant" flag is setted, then "INPUT" result is selected redirect
		this.concertInsertResult.setConcertExistant(true);
		return Execution.INPUT;
	}	
	
}
</code>
</pre>

package agentarium;

import agentarium.attributes.AttributeSetCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ModelElement} abstract class via a dummy subclass.
 */
public class ModelElementTest {

    private static class DummyModelElement extends ModelElement {
        private boolean wasRunCalled = false;

        public DummyModelElement(String name, AttributeSetCollection attributeSetCollection) {
            super(name, attributeSetCollection);
        }

        @Override
        public void run() {
            wasRunCalled = true;
        }

        public boolean wasRunCalled() {
            return wasRunCalled;
        }
    }

    private AttributeSetCollection attributeSetCollection;
    private DummyModelElement dummyElement;

    @BeforeEach
    public void setup() {
        attributeSetCollection = new AttributeSetCollection();
        dummyElement = new DummyModelElement("TestElement", attributeSetCollection);
    }

    @Test
    public void testNameIsStoredCorrectly() {
        assertEquals("TestElement", dummyElement.getName(), "Model element name should match the constructor input.");
    }

    @Test
    public void testAttributeSetCollectionIsReturned() {
        assertEquals(attributeSetCollection.getModelElementName(), dummyElement.getAttributeSetCollection().getModelElementName(),
                "Attribute set collection should be the same as passed in.");
    }

    @Test
    public void testSetupInitialisesAttributeCollection() throws Exception {
        AttributeSetCollection temp = new AttributeSetCollection();
        DummyModelElement dummy = new DummyModelElement("TestElement", temp);

        Field field = ModelElement.class.getDeclaredField("attributeSetCollection");
        field.setAccessible(true);
        AttributeSetCollection copied = (AttributeSetCollection) field.get(dummy);
        AttributeSetCollection spyCopy = spy(copied);
        field.set(dummy, spyCopy);
        
        dummy.setup();
        verify(spyCopy).setup("TestElement");
    }

    @Test
    public void testRunMethodCanBeInvoked() {
        dummyElement.run();
        assertTrue(dummyElement.wasRunCalled(), "Run method should change internal flag when invoked.");
    }

    @Test
    public void testModelElementAccessorCanBeSetAndRetrieved() {
        ModelElementAccessor mockAccessor = new ModelElementAccessor(
                dummyElement, null, null, null, null, null
        );
        dummyElement.setModelElementAccessor(mockAccessor);
        assertSame(mockAccessor, dummyElement.getModelElementAccessor(), "Accessor should be retrievable after being set.");
    }
}
